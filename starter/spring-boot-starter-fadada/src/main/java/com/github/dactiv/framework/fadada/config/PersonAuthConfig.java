package com.github.dactiv.framework.fadada.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 个人认证配置信息
 *
 * @author maurice.chen
 */
@Component
@ConfigurationProperties("dactiv.fadada.auth.person")
public class PersonAuthConfig implements Serializable  {

    @Serial
    private static final long serialVersionUID = 2355202393803772873L;

    private List<String> authScopes = List.of("ident_info","seal_info","signtask_init","signtask_info","signtask_file");

    private String faceAuthMode = "tencent";

    private List<String> nonEditableInfo = List.of("accountName", "userName", "mobile");

    private String redirectUrl;

    private String redirectMiniAppUrl;

    private boolean unbindAccount = false;

    public PersonAuthConfig() {
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

    public List<String> getNonEditableInfo() {
        return nonEditableInfo;
    }

    public void setNonEditableInfo(List<String> nonEditableInfo) {
        this.nonEditableInfo = nonEditableInfo;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectMiniAppUrl() {
        return redirectMiniAppUrl;
    }

    public void setRedirectMiniAppUrl(String redirectMiniAppUrl) {
        this.redirectMiniAppUrl = redirectMiniAppUrl;
    }

    public boolean isUnbindAccount() {
        return unbindAccount;
    }

    public void setUnbindAccount(boolean unbindAccount) {
        this.unbindAccount = unbindAccount;
    }
}
