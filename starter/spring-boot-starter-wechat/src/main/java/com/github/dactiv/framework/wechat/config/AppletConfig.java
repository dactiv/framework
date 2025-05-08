package com.github.dactiv.framework.wechat.config;

import com.github.dactiv.framework.commons.domain.metadata.RefreshAccessTokenMetadata;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@Component
@ConfigurationProperties("dactiv.wechat.applet")
public class AppletConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -5139843287077090780L;
    /**
     * 小程序账户
     */
    private RefreshAccessTokenMetadata accessToken = new RefreshAccessTokenMetadata();

    public AppletConfig() {
    }

    public RefreshAccessTokenMetadata getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(RefreshAccessTokenMetadata accessToken) {
        this.accessToken = accessToken;
    }
}
