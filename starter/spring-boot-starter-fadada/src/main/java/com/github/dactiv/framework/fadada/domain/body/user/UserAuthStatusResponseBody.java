package com.github.dactiv.framework.fadada.domain.body.user;

import java.util.List;

public class UserAuthStatusResponseBody {
    private String clientUserId;
    private String clientUserName;
    private String bindingStatus;
    private List<String> authScope;
    private String identStatus;
    private String availableStatus;
    private String openUserId;

    public UserAuthStatusResponseBody() {
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }

    public String getBindingStatus() {
        return bindingStatus;
    }

    public void setBindingStatus(String bindingStatus) {
        this.bindingStatus = bindingStatus;
    }

    public List<String> getAuthScope() {
        return authScope;
    }

    public void setAuthScope(List<String> authScope) {
        this.authScope = authScope;
    }

    public String getIdentStatus() {
        return identStatus;
    }

    public void setIdentStatus(String identStatus) {
        this.identStatus = identStatus;
    }

    public String getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(String availableStatus) {
        this.availableStatus = availableStatus;
    }

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }
}
