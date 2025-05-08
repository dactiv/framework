package com.github.dactiv.framework.wechat.service;

import com.github.dactiv.framework.commons.TimeProperties;
import com.github.dactiv.framework.commons.annotation.Time;
import com.github.dactiv.framework.commons.domain.AccessToken;
import com.github.dactiv.framework.commons.domain.metadata.RefreshAccessTokenMetadata;
import com.github.dactiv.framework.commons.exception.ErrorCodeException;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.idempotent.ConcurrentConfig;
import com.github.dactiv.framework.idempotent.advisor.concurrent.ConcurrentInterceptor;
import com.github.dactiv.framework.idempotent.annotation.Concurrent;
import com.github.dactiv.framework.nacos.task.annotation.NacosCronScheduled;
import com.github.dactiv.framework.wechat.config.WechatConfig;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.redisson.api.RBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class WechatBasicService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatBasicService.class);

    private final WechatConfig wechatConfig;

    private final RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

    private final ConcurrentInterceptor concurrentInterceptor;

    public WechatBasicService(WechatConfig wechatConfig,ConcurrentInterceptor concurrentInterceptor) {
        this.wechatConfig = wechatConfig;
        this.concurrentInterceptor = concurrentInterceptor;
    }

    protected abstract RefreshAccessTokenMetadata getRefreshAccessTokenMetadata();

    public AccessToken getAccessTokenIfCacheNull() {
        RBucket<AccessToken> bucket = concurrentInterceptor.getRedissonClient().getBucket(getRefreshAccessTokenMetadata().getCache().getName());
        AccessToken accessToken = bucket.get();
        if (Objects.isNull(accessToken)) {
            accessToken = refreshAccessToken();
        }

        return accessToken;
    }

    @NacosCronScheduled(cron = "${dactiv.wechat.refresh-access-token-cron:0 0/30 * * * ? }")
    @Concurrent(value = "dactiv:wechat:refresh-access-token" , waitTime = @Time(value = 8, unit = TimeUnit.SECONDS), leaseTime = @Time(value = 5, unit = TimeUnit.SECONDS), exception = "刷新微信 accessToken 出现并发")
    public AccessToken refreshAccessToken() {
        RefreshAccessTokenMetadata refreshAccessTokenMetadata = getRefreshAccessTokenMetadata();
        RBucket<AccessToken> bucket = concurrentInterceptor
                .getRedissonClient()
                .getBucket(refreshAccessTokenMetadata.getCache().getName());
        AccessToken token = bucket.get();
        if (Objects.nonNull(token) && System.currentTimeMillis() - token.getCreationTime().getTime() > refreshAccessTokenMetadata.getRefreshAccessTokenLeadTime().toMillis()) {
            return token;
        }

        ConcurrentConfig config = new ConcurrentConfig();

        config.setKey("dactiv:wechat:get-access-token");
        config.setException("获取微信 accessToken 出现并发");
        config.setWaitTime(TimeProperties.of(8, TimeUnit.SECONDS));
        config.setLeaseTime(TimeProperties.of(5, TimeUnit.SECONDS));

        token = concurrentInterceptor.invoke(config, this::getAccessToken);
        bucket.set(token);
        bucket.expire(token.getExpiresTime().toDuration());

        return token;
    }

    private AccessToken getCacheAccessToken() {
        RefreshAccessTokenMetadata refreshAccessTokenMetadata = getRefreshAccessTokenMetadata();
        RBucket<AccessToken> bucket = concurrentInterceptor
                .getRedissonClient()
                .getBucket(refreshAccessTokenMetadata.getCache().getName());
        return bucket.get();
    }

    /**
     * 获取微信访问 token
     *
     * @return 访问 token
     */
    private AccessToken getAccessToken() {
        AccessToken token = getCacheAccessToken();
        if (Objects.nonNull(token)) {
            return token;
        }

        Map<String, String> param = new LinkedHashMap<>();
        param.put("appid", getRefreshAccessTokenMetadata().getSecretId());
        param.put("secret", getRefreshAccessTokenMetadata().getSecretKey());
        param.put("grant_type", "client_credential");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map<String, Object>> result = restTemplate.exchange(
                "https://api.weixin.qq.com/cgi-bin/stable_token",
                HttpMethod.POST,
                new HttpEntity<>(param, httpHeaders),
                new ParameterizedTypeReference<>() {
                }
        );

        LOGGER.info("获取 wechat access token 结果为:{}", result.getBody());
        if (isSuccess(result) && Objects.requireNonNull(result.getBody()).containsKey("access_token")) {
            token = new AccessToken();
            token.setToken(result.getBody().get("access_token").toString());
            token.setExpiresTime(TimeProperties.of(NumberUtils.toInt(result.getBody().get("expires_in").toString()), TimeUnit.SECONDS));
        } else {
            throwSystemExceptionIfError(result.getBody());
        }

        return token;
    }

    /**
     * 是否调用成功
     *
     * @param result 响应实体
     * @return true 是，否则 false
     */
    public boolean isSuccess(ResponseEntity<Map<String, Object>> result) {

        if (!HttpStatus.OK.equals(result.getStatusCode())) {
            return false;
        }

        if (MapUtils.isEmpty(result.getBody())) {
            return false;
        }

        if (result.getBody().containsKey(wechatConfig.getStatusCodeFieldName())) {
            String statusCode = result.getBody().get(wechatConfig.getStatusCodeFieldName()).toString();
            return statusCode.equals(wechatConfig.getSuccessCodeValue());
        }

        return true;
    }

    /**
     * 如果响应内容错误，抛出异常
     *
     * @param result 响应数据
     */
    public void throwSystemExceptionIfError(Map<String, Object> result) {
        if (MapUtils.isNotEmpty(result)) {
            throw new ErrorCodeException(result.get(wechatConfig.getStatusMessageFieldName()).toString(), result.get(wechatConfig.getStatusCodeFieldName()).toString());
        } else {
            throw new SystemException("执行微信 api 接口出现异常");
        }
    }

    public WechatConfig getWechatConfig() {
        return wechatConfig;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public ConcurrentInterceptor getConcurrentInterceptor() {
        return concurrentInterceptor;
    }
}
