package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.TimeProperties;
import com.github.dactiv.framework.commons.annotation.Time;
import com.github.dactiv.framework.commons.domain.AccessToken;
import com.github.dactiv.framework.commons.domain.metadata.RefreshAccessTokenMetadata;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.body.ResponseBody;
import com.github.dactiv.framework.idempotent.ConcurrentConfig;
import com.github.dactiv.framework.idempotent.advisor.concurrent.ConcurrentInterceptor;
import com.github.dactiv.framework.idempotent.annotation.Concurrent;
import com.github.dactiv.framework.nacos.task.annotation.NacosCronScheduled;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthService extends FadadaBasicService implements InitializingBean {

    private final ConcurrentInterceptor interceptor;

    public AuthService(FadadaConfig fadadaConfig, ConcurrentInterceptor interceptor, RestTemplate restTemplate) {
        super(fadadaConfig, restTemplate);
        this.interceptor = interceptor;
    }

    @Async
    @Override
    public void afterPropertiesSet() throws Exception {
        AccessToken accessToken = getAccessTokenIfCacheNull();
        LOGGER.info("[法大大] 当前 token 为: {}, 在: {} 后超时", accessToken.getToken(), accessToken.getExpiresInDateTime());
    }

    public AccessToken getAccessTokenIfCacheNull() {
        AccessToken accessToken = getCacheAccessToken();
        if (Objects.isNull(accessToken)) {
            accessToken = refreshAccessToken();
        }
        return accessToken;
    }

    @NacosCronScheduled(cron = "${dactiv.fadada.refresh-access-token-cron:0 0/30 * * * ? }")
    @Concurrent(value = "dactiv:fadada:refresh-access-token" , waitTime = @Time(value = 8, unit = TimeUnit.SECONDS), leaseTime = @Time(value = 5, unit = TimeUnit.SECONDS), exception = "刷新法大大 accessToken 出现并发")
    public AccessToken refreshAccessToken() {
        AccessToken token = getCacheAccessToken();
        RefreshAccessTokenMetadata refreshAccessToken = getFadadaConfig().getAccessToken();
        if (Objects.nonNull(token) && System.currentTimeMillis() - token.getCreationTime().getTime() > refreshAccessToken.getRefreshAccessTokenLeadTime().toMillis()) {
            return token;
        }

        ConcurrentConfig config = new ConcurrentConfig();

        config.setKey("dactiv:fadada:get-access-token");
        config.setException("获取发大大 accessToken 出现并发");
        config.setWaitTime(TimeProperties.of(8, TimeUnit.SECONDS));
        config.setLeaseTime(TimeProperties.of(5, TimeUnit.SECONDS));

        token = interceptor.invoke(config, this::getAccessToken);
        RBucket<AccessToken> bucket = interceptor
                .getRedissonClient()
                .getBucket(getFadadaConfig().getAccessToken().getCache().getName());
        bucket.set(token);
        bucket.expire(token.getExpiresTime().toDuration());
        return token;
    }

    private AccessToken getCacheAccessToken(){
        RBucket<AccessToken> bucket = interceptor
                .getRedissonClient()
                .getBucket(getFadadaConfig().getAccessToken().getCache().getName());
        return bucket.get();
    }

    private AccessToken getAccessToken() {
        AccessToken accessToken = getCacheAccessToken();
        if (Objects.nonNull(accessToken)) {
            return accessToken;
        }
        HttpHeaders headers = createBasicParam();
        headers.add(FadadaConfig.DEFAULT_FASC_GRANT_TYPE_HEADER_NAME, "client_credential");
        sign(headers);
        String url = "/service/get-access-token";

        ResponseEntity<Map<String, Object>> response = getRestTemplate().exchange(
                getFadadaConfig().getBaseUrl() + "/service/get-access-token",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );

        ResponseBody<Map<String, Object>> data = assertAndGetData(response, url);
        accessToken = new AccessToken();
        accessToken.setToken(data.getData().get("accessToken").toString());
        accessToken.setExpiresTime(TimeProperties.of(Long.parseLong(data.getData().get("expiresIn").toString()), TimeUnit.SECONDS));

        return accessToken;
    }
}
