package com.github.dactiv.framework.allin.pay.service;

import com.github.dactiv.framework.allin.pay.config.AllInPayProperties;
import com.github.dactiv.framework.allin.pay.domain.body.UnifiedRequestBody;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.exception.ErrorCodeException;
import com.github.dactiv.framework.commons.exception.SystemException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Service
public class AllInPayService {

    public static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    public static final Logger LOGGER = LoggerFactory.getLogger(AllInPayService.class);

    private final AllInPayProperties allInPayConfig;

    private final RestTemplate restTemplate;

    public AllInPayService(AllInPayProperties allInPayConfig, RestTemplate restTemplate) {
        this.allInPayConfig = allInPayConfig;
        this.restTemplate = restTemplate;
    }

    private String sign(UnifiedRequestBody data) {
        Map<String, Object> singData = new TreeMap<>(Casts.convertValue(data, Casts.MAP_TYPE_REFERENCE));
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : singData.keySet()) {
            stringBuilder.append(key).append(singData.get(key));
        }
        stringBuilder.append(allInPayConfig.getSecret().getSecretKey());
        return DigestUtils.md5DigestAsHex(stringBuilder.toString().getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }

    public Map<String, Object> unifiedPay(UnifiedRequestBody metadata) {
        metadata.setMerchantNo(allInPayConfig.getSecret().getSecretId());
        metadata.setSign(sign(metadata));
        Map<String, Object> map = Casts.convertValue(metadata, Casts.MAP_TYPE_REFERENCE);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        map.forEach((k,v) -> data.add(k,v.toString()));
        String param = Casts.castRequestBodyMapToString(data);
        return exchange(allInPayConfig.getBaseUrl() + AllInPayProperties.UNIFIED_PAY_API + Casts.QUESTION_MARK + param, HttpMethod.GET, new HttpEntity<>(null,null));
    }

    public Map<String, Object> exchange(String url, HttpMethod method, HttpEntity<?> entity) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                method,
                entity,
                new ParameterizedTypeReference<>() {}
        );
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[通联支付]: 调用 {} 接口, 提交的参数为:{}", url, Objects.nonNull(entity.getBody()) ? Casts.convertValue(entity.getBody(), Casts.MAP_TYPE_REFERENCE) : StringUtils.EMPTY);
        }

        SystemException.isTrue(response.getStatusCode().is2xxSuccessful(), HttpStatus.valueOf(response.getStatusCode().value()).getReasonPhrase());

        Map<String, Object> body = response.getBody();
        SystemException.isTrue(MapUtils.isNotEmpty(body), "[通联支付]: 找不到 " + url + " 接口的响应体信息");
        String code = Objects.toString(body.get(allInPayConfig.getApiResultCodeField()), StringUtils.EMPTY);
        String message = Objects.toString(body.get(allInPayConfig.getApiResultMessageField()), StringUtils.EMPTY);
        String errorCode = Objects.toString(body.get(allInPayConfig.getApiResultErrorCodeField()), StringUtils.EMPTY);

        SystemException.isTrue(StringUtils.equals(code, allInPayConfig.getApiCodeSuccessValue()), () -> new ErrorCodeException("[通联支付]:" + message, errorCode));

        return body;
    }

    public AllInPayProperties getAllInPayConfig() {
        return allInPayConfig;
    }
}
