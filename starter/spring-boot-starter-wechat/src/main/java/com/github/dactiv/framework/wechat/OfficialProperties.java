package com.github.dactiv.framework.wechat;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.domain.metadata.RefreshAccessTokenMetadata;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@Component
@ConfigurationProperties("dactiv.wechat.official")
public class OfficialProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = -7783628591893033115L;

    /**
     * 小程序账户
     */
    private RefreshAccessTokenMetadata accessToken = new RefreshAccessTokenMetadata(
            CacheProperties.of("dactiv:wechat:official:access-token")
    );

    public OfficialProperties() {
    }

    public RefreshAccessTokenMetadata getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(RefreshAccessTokenMetadata accessToken) {
        this.accessToken = accessToken;
    }
}
