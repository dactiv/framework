package com.github.dactiv.framework.fadada.domain.body.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class GetUserAuthUrlRequestBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 1704706448016846911L;

    private String clientUserId;
    private String userName;
    private String userIdentType;
    private String userIdentNo;
    private Boolean userIdentInfoMatch;
    private List<String> authScopes;
    private String redirectUrl;
    private String redirectMiniAppUrl;
    private String accountName;
    private GetUserAuthIdentRequestBody userIdentInfo;
    private List<String> nonEditableInfo;
    private Boolean unbindAccount;
    private GetUserAuthFreeSignRequestBody freeSignInfo;

    public GetUserAuthUrlRequestBody() {

    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdentType() {
        return userIdentType;
    }

    public void setUserIdentType(String userIdentType) {
        this.userIdentType = userIdentType;
    }

    public String getUserIdentNo() {
        return userIdentNo;
    }

    public void setUserIdentNo(String userIdentNo) {
        this.userIdentNo = userIdentNo;
    }

    public Boolean getUserIdentInfoMatch() {
        return userIdentInfoMatch;
    }

    public void setUserIdentInfoMatch(Boolean userIdentInfoMatch) {
        this.userIdentInfoMatch = userIdentInfoMatch;
    }

    public List<String> getAuthScopes() {
        return authScopes;
    }

    public void setAuthScopes(List<String> authScopes) {
        this.authScopes = authScopes;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public GetUserAuthIdentRequestBody getUserIdentInfo() {
        return userIdentInfo;
    }

    public void setUserIdentInfo(GetUserAuthIdentRequestBody userIdentInfo) {
        this.userIdentInfo = userIdentInfo;
    }

    public List<String> getNonEditableInfo() {
        return nonEditableInfo;
    }

    public void setNonEditableInfo(List<String> nonEditableInfo) {
        this.nonEditableInfo = nonEditableInfo;
    }

    public Boolean getUnbindAccount() {
        return unbindAccount;
    }

    public void setUnbindAccount(Boolean unbindAccount) {
        this.unbindAccount = unbindAccount;
    }

    public GetUserAuthFreeSignRequestBody getFreeSignInfo() {
        return freeSignInfo;
    }

    public void setFreeSignInfo(GetUserAuthFreeSignRequestBody freeSignInfo) {
        this.freeSignInfo = freeSignInfo;
    }
}
