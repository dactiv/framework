package com.github.dactiv.framework.fadada.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.TimeProperties;
import com.github.dactiv.framework.commons.domain.AccessToken;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.crypto.algorithm.SimpleByteSource;
import com.github.dactiv.framework.crypto.algorithm.hash.Hash;
import com.github.dactiv.framework.crypto.algorithm.hash.HashAlgorithmMode;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.ResponseBody;
import com.github.dactiv.framework.fadada.domain.body.doc.FileProcessRequestBody;
import com.github.dactiv.framework.fadada.domain.body.doc.FileProcessResponseBody;
import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlRequestBody;
import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlResponseBody;
import com.github.dactiv.framework.fadada.domain.body.task.CreateSignTaskResponseBody;
import com.github.dactiv.framework.fadada.domain.body.task.CreateSignTaskWithTemplateRequestBody;
import com.github.dactiv.framework.fadada.domain.body.template.CreateDocTemplateRequestBody;
import com.github.dactiv.framework.fadada.domain.body.template.CreateDocTemplateResponseBody;
import com.github.dactiv.framework.fadada.domain.body.template.GetTemplateEditUrlRequestBody;
import com.github.dactiv.framework.fadada.domain.body.template.GetTemplateEditUrlResponseBody;
import com.github.dactiv.framework.idempotent.annotation.Concurrent;
import com.github.dactiv.framework.nacos.task.annotation.NacosCronScheduled;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FadadaService implements InitializingBean {

    public static final Logger LOGGER = LoggerFactory.getLogger(FadadaService.class);

    private final FadadaConfig fadadaConfig;

    private final RedissonClient redissonClient;

    private final RestTemplate restTemplate;

    public FadadaService(FadadaConfig fadadaConfig, RedissonClient redissonClient, RestTemplate restTemplate) {
        this.fadadaConfig = fadadaConfig;
        this.redissonClient = redissonClient;
        this.restTemplate = restTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AccessToken accessToken = getCacheAccessToken();
        LOGGER.info("[法大大] 当前 token 为: {}, 在: {} 后超时", accessToken.getToken(), accessToken.getExpiresInDateTime());
    }

    public GetUploadUrlResponseBody getUploadUrl(GetUploadUrlRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/file/get-upload-url", param, GetUploadUrlResponseBody.class);
    }

    public FileProcessResponseBody processFile(FileProcessRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/file/process", param, FileProcessResponseBody.class);
    }

    public CreateDocTemplateResponseBody createTemplate(CreateDocTemplateRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/doc-template/create", param, CreateDocTemplateResponseBody.class);
    }

    public GetTemplateEditUrlResponseBody getTemplateEditUrl(GetTemplateEditUrlRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/template/edit/get-url", param, GetTemplateEditUrlResponseBody.class);
    }

    public CreateSignTaskResponseBody createSignTaskWithTemplate(CreateSignTaskWithTemplateRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/sign-task/create-with-template", param, CreateSignTaskResponseBody.class);
    }

    public AccessToken getCacheAccessToken() {
        RBucket<AccessToken> bucket = redissonClient.getBucket(fadadaConfig.getAccessToken().getName());
        AccessToken accessToken = bucket.get();
        if (Objects.isNull(accessToken)) {
            accessToken = refreshAccessToken();
        }

        return accessToken;
    }

    public <T> T executeApi(String url, Map<String, Object> params, Class<T> responseType) {
        String bizContent = SystemException.convertSupplier(() -> Casts.getObjectMapper().writeValueAsString(params));
        HttpHeaders headers = fadadaConfig.createBasicParam();
        headers.add("X-FASC-AccessToken", getCacheAccessToken().getToken());
        headers.add("bizContent", bizContent);
        sign(headers);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("bizContent", bizContent);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                fadadaConfig.getBaseUrl() + url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                new ParameterizedTypeReference<>() {}
        );

        ResponseBody<Map<String, Object>> result = assertAndGetData(response, url);

        return Casts.convertValue(result.getData(), responseType);
    }

    public AccessToken getAccessToken() {
        HttpHeaders headers = fadadaConfig.createBasicParam();
        headers.add("X-FASC-Grant-Type", "client_credential");
        sign(headers);
        String url = "/service/get-access-token";

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                fadadaConfig.getBaseUrl() + "/service/get-access-token",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );

        ResponseBody<Map<String, Object>> data = assertAndGetData(response, url);
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(data.getData().get("accessToken").toString());
        accessToken.setExpiresTime(TimeProperties.of(Long.parseLong(data.getData().get("expiresIn").toString()), TimeUnit.SECONDS));

        return accessToken;
    }

    public  ResponseBody<Map<String, Object>> assertAndGetData(ResponseEntity<Map<String, Object>> response, String url) {

        SystemException.isTrue(
                response.getStatusCode().is2xxSuccessful(),
                "[法大大: " + response.getStatusCode().value() + "] 执行 [" + url + "] 时出现异常"
        );

        Map<String, Object> body = Objects.requireNonNull(response.getBody(),"[法大大: ] 执行 [" + url + "] response entity 中 body 为空");
        ResponseBody<Map<String, Object>> result = Casts.convertValue(body, new TypeReference<>() {});
        SystemException.isTrue(!result.isSuccess(), result.getMsg());
        return result;
    }

    @NacosCronScheduled(cron = "${dactiv.fadada.refresh-access-token-cron:0 0/30 * * * ? }")
    @Concurrent(value = "dactiv:fadada:refresh-access-token", exception = "刷新法大大 accessToken 出现并发")
    public AccessToken refreshAccessToken() {
        AccessToken token = getAccessToken();
        RBucket<AccessToken> bucket = redissonClient.getBucket(fadadaConfig.getAccessToken().getName());
        bucket.set(token);
        bucket.expire(token.getExpiresTime().toDuration());
        return token;
    }

    public void sign(HttpHeaders headers) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        headers.add("X-FASC-Timestamp", timestamp);

        // 获取 MultiValueMap 中的所有键
        List<String> keys = new ArrayList<>(headers.keySet());
        // 对键进行升序排序
        Collections.sort(keys);

        MultiValueMap<String, String> sortedMultiValueMap = new LinkedMultiValueMap<>();
        keys.forEach(key -> sortedMultiValueMap.put(key, headers.get(key)));

        String paramString = Casts.castRequestBodyMapToString(sortedMultiValueMap);
        String signText = new Hash(HashAlgorithmMode.SHA256.getName(), paramString, null, -1).getHex().toLowerCase();
        byte[] secretSigning = hmac256(fadadaConfig.getSecret().getSecretKey().getBytes(StandardCharsets.UTF_8),timestamp);
        String sign = new SimpleByteSource(hmac256(secretSigning, signText)).getHex();
        headers.add("X-FASC-Sign", sign);
    }

    public byte[] hmac256(byte[] key, String msg) {
        return SystemException.convertSupplier(() -> {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
            mac.init(secretKeySpec);
            return mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
        }, "基于 key: " + new String(key, StandardCharsets.UTF_8) + ", 加密:" + msg + "时，出现异常");
    }
}
