package com.github.dactiv.framework.wechat.config;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.domain.metadata.RefreshAccessTokenMetadata;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author maurice.chen
 */
@Component
@ConfigurationProperties("dactiv.wechat.applet")
public class AppletConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -5139843287077090780L;

    public static final String DEFAULT_PHONE_NUMBER_CODE_PARAM_NAME = "phoneNumberCode";

    /**
     * 手机号码参数名称
     */
    private String phoneNumberCodeParamName = DEFAULT_PHONE_NUMBER_CODE_PARAM_NAME;

    /**
     * 小程序账户
     */
    private RefreshAccessTokenMetadata accessToken = new RefreshAccessTokenMetadata(
            CacheProperties.of("dactiv:wechat:applet:access-token")
    );

    public AppletConfig() {
    }

    public String getPhoneNumberCodeParamName() {
        return phoneNumberCodeParamName;
    }

    public void setPhoneNumberCodeParamName(String phoneNumberCodeParamName) {
        this.phoneNumberCodeParamName = phoneNumberCodeParamName;
    }

    public RefreshAccessTokenMetadata getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(RefreshAccessTokenMetadata accessToken) {
        this.accessToken = accessToken;
    }
}
