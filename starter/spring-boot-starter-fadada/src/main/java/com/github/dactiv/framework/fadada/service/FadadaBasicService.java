package com.github.dactiv.framework.fadada.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.crypto.algorithm.SimpleByteSource;
import com.github.dactiv.framework.crypto.algorithm.hash.Hash;
import com.github.dactiv.framework.crypto.algorithm.hash.HashAlgorithmMode;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.body.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class FadadaBasicService {

    public static final Logger LOGGER = LoggerFactory.getLogger(FadadaBasicService.class);

    private final FadadaConfig fadadaConfig;

    private final RestTemplate restTemplate;

    public FadadaBasicService(FadadaConfig fadadaConfig, RestTemplate restTemplate) {
        this.fadadaConfig = fadadaConfig;
        this.restTemplate = restTemplate;
    }

    protected <T> T executeApi(String url, String accessToken, Map<String, Object> params, Class<T> responseType) {
        String bizContent = SystemException.convertSupplier(() -> Casts.getObjectMapper().writeValueAsString(params));
        HttpHeaders headers = fadadaConfig.createBasicParam();
        headers.add("X-FASC-AccessToken", accessToken);
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

    protected ResponseBody<Map<String, Object>> assertAndGetData(ResponseEntity<Map<String, Object>> response, String url) {

        SystemException.isTrue(
                response.getStatusCode().is2xxSuccessful(),
                "[法大大: " + response.getStatusCode().value() + "] 执行 [" + url + "] 时出现异常"
        );

        Map<String, Object> body = Objects.requireNonNull(response.getBody(),"[法大大: ] 执行 [" + url + "] response entity 中 body 为空");
        ResponseBody<Map<String, Object>> result = Casts.convertValue(body, new TypeReference<>() {});
        result.setRequestId(Objects.toString(response.getHeaders().getFirst("X-FASC-Request-Id"), StringUtils.EMPTY));
        result.setHttpStatusCode(response.getStatusCode().value());
        SystemException.isTrue(result.isSuccess(), result.getMsg());
        return result;
    }

    protected void sign(HttpHeaders headers) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        headers.add("X-FASC-Timestamp", timestamp);
        String sign = generateSignString(headers, timestamp);
        headers.add("X-FASC-Sign", sign);
    }

    public String generateSignString(HttpHeaders headers, String timestamp) {
        // 获取 MultiValueMap 中的所有键
        List<String> keys = new ArrayList<>(headers.keySet());
        // 对键进行升序排序
        Collections.sort(keys);

        MultiValueMap<String, String> sortedMultiValueMap = new LinkedMultiValueMap<>();
        keys.forEach(key -> sortedMultiValueMap.put(key, headers.get(key)));
        return generateSignString(sortedMultiValueMap, timestamp);
    }

    public String generateSignString(Map<String, String> params, String timestamp) {
        // 获取 MultiValueMap 中的所有键
        Map<String, String> treeMap = new TreeMap<>(params);

        MultiValueMap<String, String> sortedMultiValueMap = new LinkedMultiValueMap<>();
        treeMap.forEach(sortedMultiValueMap::add);

        return generateSignString(sortedMultiValueMap, timestamp);
    }

    public String generateSignString(MultiValueMap<String, String> sortedMap, String timestamp) {
        String paramString = Casts.castRequestBodyMapToString(sortedMap);
        String signText = new Hash(HashAlgorithmMode.SHA256.getName(), paramString, null, -1).getHex().toLowerCase();
        byte[] secretSigning = hmac256(fadadaConfig.getSecret().getSecretKey().getBytes(StandardCharsets.UTF_8),timestamp);
        return new SimpleByteSource(hmac256(secretSigning, signText)).getHex();
    }

    public byte[] hmac256(byte[] key, String msg) {
        return SystemException.convertSupplier(() -> {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
            mac.init(secretKeySpec);
            return mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
        }, "基于 key: " + new String(key, StandardCharsets.UTF_8) + ", 加密:" + msg + "时，出现异常");
    }

    public FadadaConfig getFadadaConfig() {
        return fadadaConfig;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
