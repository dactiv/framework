package com.github.dactiv.framework.fadada.config;

import com.github.dactiv.framework.commons.TimeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component
@ConfigurationProperties("dactiv.fadada.auth.sign-task")
public class SignTaskConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 771180693570577131L;

    private TimeProperties expiresTime = TimeProperties.of(7, TimeUnit.DAYS);

    private Boolean autoStart = true;

    private Boolean autoFillFinalize = true;

    private String callbackUrl;

    public SignTaskConfig() {
    }

    public Boolean getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    public Boolean getAutoFillFinalize() {
        return autoFillFinalize;
    }

    public void setAutoFillFinalize(Boolean autoFillFinalize) {
        this.autoFillFinalize = autoFillFinalize;
    }

    public TimeProperties getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(TimeProperties expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
