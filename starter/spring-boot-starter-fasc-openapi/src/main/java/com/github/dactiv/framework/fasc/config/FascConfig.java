package com.github.dactiv.framework.fasc.config;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.domain.metadata.RefreshAccessTokenMetadata;
import com.github.dactiv.framework.fasc.enums.http.SignTypeEnum;
import com.github.dactiv.framework.fasc.stratey.DefaultJsonStrategy;
import com.github.dactiv.framework.fasc.stratey.JsonStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author maurice.chen
 */
@ConfigurationProperties("dactiv.fasc")
public class FascConfig {

    private RefreshAccessTokenMetadata accessToken  = new RefreshAccessTokenMetadata(
            CacheProperties.of("dactiv:fasc:access-token")
    );

    private String openCorpId;

    private List<String> authScopes = List.of("ident_info","seal_info","signtask_init","signtask_info","signtask_file");

    private String faceAuthMode = "tencent";

    private String serverUrl;

    /**
     * 请求服务端签名加密方式，目前仅支持HMAC-SHA256，请勿修改
     */
    private String signType = SignTypeEnum.HMAC_SHA256.getType();

    /**
     * json串策率
     */
    private JsonStrategy jsonStrategy = new DefaultJsonStrategy();

    /**
     * http配置
     */
    private HttpConfig httpConfig;

    private String successCodeValue = "100000";

    public FascConfig() {
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public JsonStrategy getJsonStrategy() {
        return jsonStrategy;
    }

    public void setJsonStrategy(JsonStrategy jsonStrategy) {
        this.jsonStrategy = jsonStrategy;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    public RefreshAccessTokenMetadata getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(RefreshAccessTokenMetadata accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
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

    public String getSuccessCodeValue() {
        return successCodeValue;
    }

    public void setSuccessCodeValue(String successCodeValue) {
        this.successCodeValue = successCodeValue;
    }
}
