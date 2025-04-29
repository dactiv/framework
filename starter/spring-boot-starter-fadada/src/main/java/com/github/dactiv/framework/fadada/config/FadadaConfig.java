package com.github.dactiv.framework.fadada.config;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.domain.metadata.CloudSecretMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConfigurationProperties("dactiv.fadada")
public class FadadaConfig {

    private CloudSecretMetadata secret = new CloudSecretMetadata();

    private String clientCorpId;

    private String openCorpId;

    private String baseUrl;

    private CacheProperties accessToken = CacheProperties.of("dactiv:fadada:access-token");

    private String redirectUrl;

    public FadadaConfig() {
    }

    public CloudSecretMetadata getSecret() {
        return secret;
    }

    public void setSecret(CloudSecretMetadata secret) {
        this.secret = secret;
    }

    public String getClientCorpId() {
        return clientCorpId;
    }

    public void setClientCorpId(String clientCorpId) {
        this.clientCorpId = clientCorpId;
    }

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public CacheProperties getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(CacheProperties accessToken) {
        this.accessToken = accessToken;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public HttpHeaders createBasicParam() {

        HttpHeaders handler = new HttpHeaders();

        handler.add("X-FASC-App-Id", secret.getSecretId());
        handler.add("X-FASC-Sign-Type", "HMAC-SHA256");
        handler.add("X-FASC-Nonce", UUID.randomUUID().toString().replaceAll(Casts.NEGATIVE, StringUtils.EMPTY));
        handler.add("X-FASC-Api-SubVersion", "5.1");

        return handler;
    }


}
