package com.github.dactiv.framework.fadada.config;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.domain.metadata.RefreshAccessTokenMetadata;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("dactiv.fadada")
public class FadadaConfig {

    public static final String DEFAULT_FASC_GRANT_TYPE_HEADER_NAME = "X-FASC-Grant-Type";

    public static final String DEFAULT_FASC_APP_ID_HEADER_NAME = "X-FASC-App-Id";

    public static final String DEFAULT_FASC_SIGN_TYPE_HEADER_NAME = "X-FASC-Sign-Type";

    public static final String DEFAULT_FASC_NONCE_HEADER_NAME = "X-FASC-Nonce";

    public static final String DEFAULT_FASC_API_SUBVERSION_HEADER_NAME = "X-FASC-Api-SubVersion";

    public static final String DEFAULT_FASC_TIMESTAMP_HEADER_NAME = "X-FASC-Timestamp";

    public static final String DEFAULT_FASC_SIGN_HEADER_NAME = "X-FASC-Sign";

    public static final String DEFAULT_FASC_ACCESS_TOKEN_HEADER_NAME = "X-FASC-AccessToken";

    public static final String DEFAULT_FASC_EVENT_HEADER_NAME = "X-FASC-Event";

    public static final String DEFAULT_BIZ_CONTENT_HEADER_NAME = "bizContent";

    private String clientCorpId;

    private String openCorpId;

    private String baseUrl;

    private RefreshAccessTokenMetadata accessToken = new RefreshAccessTokenMetadata(CacheProperties.of("dactiv:fadada:access-token"));

    private String redirectUrl;

    private List<String> authScopes = List.of("ident_info","seal_info","signtask_init","signtask_info","signtask_file");

    private String faceAuthMode = "tencent";

    public FadadaConfig() {
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

    public RefreshAccessTokenMetadata getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(RefreshAccessTokenMetadata accessToken) {
        this.accessToken = accessToken;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public List<String> getAuthScopes() {
        return authScopes;
    }

    public void setAuthScopes(List<String> authScopes) {
        this.authScopes = authScopes;
    }

    public String getFaceAuthMode() {
        return faceAuthMode;
    }

    public void setFaceAuthMode(String faceAuthMode) {
        this.faceAuthMode = faceAuthMode;
    }
}
