package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.TimeProperties;
import com.github.dactiv.framework.commons.domain.AccessToken;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.body.ResponseBody;
import com.github.dactiv.framework.idempotent.annotation.Concurrent;
import com.github.dactiv.framework.nacos.task.annotation.NacosCronScheduled;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthService extends FadadaBasicService implements InitializingBean {

    private final RedissonClient redissonClient;

    public AuthService(FadadaConfig fadadaConfig, RedissonClient redissonClient, RestTemplate restTemplate) {
        super(fadadaConfig, restTemplate);
        this.redissonClient = redissonClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AccessToken accessToken = getCacheAccessToken();
        LOGGER.info("[法大大] 当前 token 为: {}, 在: {} 后超时", accessToken.getToken(), accessToken.getExpiresInDateTime());
    }

    public AccessToken getCacheAccessToken() {
        RBucket<AccessToken> bucket = redissonClient.getBucket(getFadadaConfig().getAccessToken().getName());
        AccessToken accessToken = bucket.get();
        if (Objects.isNull(accessToken)) {
            accessToken = refreshAccessToken();
        }

        return accessToken;
    }

    @NacosCronScheduled(cron = "${dactiv.fadada.refresh-access-token-cron:0 0/30 * * * ? }")
    @Concurrent(value = "dactiv:fadada:refresh-access-token", exception = "刷新法大大 accessToken 出现并发")
    public AccessToken refreshAccessToken() {
        AccessToken token = getAccessToken();
        RBucket<AccessToken> bucket = redissonClient.getBucket(getFadadaConfig().getAccessToken().getName());
        bucket.set(token);
        bucket.expire(token.getExpiresTime().toDuration());
        return token;
    }

    public AccessToken getAccessToken() {
        HttpHeaders headers = getFadadaConfig().createBasicParam();
        headers.add("X-FASC-Grant-Type", "client_credential");
        sign(headers);
        String url = "/service/get-access-token";

        ResponseEntity<Map<String, Object>> response = getRestTemplate().exchange(
                getFadadaConfig().getBaseUrl() + "/service/get-access-token",
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
}
