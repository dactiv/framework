package com.github.dactiv.framework.canal.resolver.support;

import com.github.dactiv.framework.canal.domain.meta.HttpCanalRowDataChangeNoticeMetadata;
import com.github.dactiv.framework.canal.resolver.CanalRowDataChangeNoticeResolver;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.commons.domain.AckMessage;
import com.github.dactiv.framework.commons.domain.body.AckResponseBody;
import com.github.dactiv.framework.commons.enumerate.support.AckStatus;
import com.github.dactiv.framework.commons.enumerate.support.ExecuteStatus;
import com.github.dactiv.framework.commons.enumerate.support.Protocol;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * http 形式 canal 行数据变更通知实现
 *
 * @author maurice.chen
 */
public class HttpCanalRowDataChangeNoticeResolver implements CanalRowDataChangeNoticeResolver {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpCanalRowDataChangeNoticeResolver.class);

    public static final String APPEND_BODY_KEY = "appendBody";

    private RestTemplate restTemplate;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(16);

    public HttpCanalRowDataChangeNoticeResolver() {
    }

    public HttpCanalRowDataChangeNoticeResolver(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpCanalRowDataChangeNoticeResolver(RestTemplate restTemplate, ScheduledExecutorService executorService) {
        this.restTemplate = restTemplate;
        this.executorService = executorService;
    }

    @Override
    public boolean isSupport(AckMessage entity) {
        return Protocol.HTTP_OR_HTTPS.equals(entity.getProtocol());
    }

    @Override
    public void send(AckMessage entity, Consumer<AckMessage> consumer) {
        // FIXME 这里需要优化，写得不够好，Consumer<AckMessage> consumer 能不能一步返回给实现者去处理？
        CompletableFuture
                .supplyAsync(() -> exchangeNotification(entity))
                .thenAccept(result -> completableExchange(result, entity, consumer));
    }

    /**
     * 执行 http 调用
     *
     * @param entity 通知记录实体
     * @return 执行结果
     */
    private ResponseEntity<Map<String, Object>> exchangeNotification(AckMessage entity) {
        HttpCanalRowDataChangeNoticeMetadata meta = Casts.convertValue(
                entity.getProtocolMeta(),
                HttpCanalRowDataChangeNoticeMetadata.class
        );

        String url = meta.getUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (MapUtils.isNotEmpty(meta.getHeaders())) {
            meta.getHeaders().forEach(headers::add);
        }

        if (MapUtils.isNotEmpty(meta.getQueryParams())) {
            Map<String, String[]> map = Casts.cast(meta.getQueryParams());
            MultiValueMap<String, String> valueMap = Casts.castMapToMultiValueMap(map);
            String query = Casts.castRequestBodyMapToString(valueMap);
            url = addQueryParam(url, query);
        }

        Map<String, Object> body = new LinkedHashMap<>(entity.getRequestBody());
        if (MapUtils.isNotEmpty(meta.getBody())) {
            body.put(APPEND_BODY_KEY, meta.getBody());
        }

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "发送 [{}] canal 行数据通知，地址为: {}, 请求数据为:{}",
                    entity.getProtocol().getName(),
                    meta.getUrl(),
                    entity.getRequestBody()
            );
        }

        try {
            return restTemplate.exchange(url, HttpMethod.POST, httpEntity, Casts.MAP_PARAMETERIZED_TYPE_REFERENCE);
        } catch (Exception e) {
            RestResult<?> result = RestResult.ofException(e);
            result.setMessage(result.getMessage());
            Map<String, Object> data = Casts.convertValue(result, Casts.MAP_TYPE_REFERENCE);
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String addQueryParam(String url, String query) {
        if (StringUtils.contains(url, Casts.QUESTION_MARK)) {
            return url + Casts.HTTP_AND + query;
        } else {
            return url + Casts.QUESTION_MARK + query;
        }
    }

    /**
     * 完成发送
     *
     * @param result restTemplate 响应实体
     * @param entity 通知记录实体
     */
    public void completableExchange(ResponseEntity<Map<String, Object>> result,
                                    AckMessage entity,
                                    Consumer<AckMessage> consumer) {

        HttpCanalRowDataChangeNoticeMetadata meta = Casts.convertValue(
                entity.getProtocolMeta(),
                HttpCanalRowDataChangeNoticeMetadata.class
        );
        // 1.如果不成功记录错误信息。
        // 2.如果成功看看响应的结果是否有 ack 确认信息，
        //   如果什么都不返回，自动 ack，否则获取响应的 ack 值，
        //   让后续业务可以针对这个值去做补发或自动重试处理。
        if (result.getStatusCode().is2xxSuccessful()) {
            ExecuteStatus.success(entity);
            try {
                AckResponseBody body = Casts.convertValue(result.getBody(), AckResponseBody.class);
                if (Objects.nonNull(body)) {
                    entity.setResponseBody(body);
                } else {
                    entity.setResponseBody(new AckResponseBody(AckStatus.ACKNOWLEDGED));
                }
            } catch (Exception ignored) {
                entity.setResponseBody(new AckResponseBody(AckStatus.ACKNOWLEDGED, result.getBody()));
            }
        } else {
            String msg = "执行 [" + meta.getUrl() + "] 响应状态为:" + result.getStatusCode() + ", 响应体为:" + result.getBody();
            ExecuteStatus.retry(entity, msg, true);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "发送 [{}] canal 行数据通知，地址为: {}, 响应数据为:{}, 异常信息为:{}",
                    entity.getProtocol().getName(),
                    meta.getUrl(),
                    entity.getResponseBody(),
                    entity.getException()
            );
        }

        consumer.accept(entity);
        // 如果执行失败的情况下等待 consumer.accept(entity); 后继续补发。
        if (ExecuteStatus.EXECUTING_STATUS.contains(entity.getExecuteStatus())) {
            resend(entity, consumer);
        }
    }

    private void resend(AckMessage entity, Consumer<AckMessage> consumer) {
        if (entity.getRetryCount() > entity.getMaxRetryCount()) {
            return ;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("记录 [{}] 状态发送结果为: {}, 将在{} 后重新发送", entity, entity.getExecuteStatus(), entity.getNextRetryTime());
        }
        executorService.schedule(() -> send(entity, consumer), entity.getNextRetryTime().getTime(), TimeUnit.MICROSECONDS);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }
}
