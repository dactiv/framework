package com.github.dactiv.framework.fadada.domain.body.user;

import com.github.dactiv.framework.fadada.domain.metadata.user.ClientUserIdRequestMetadata;

import java.io.Serial;
import java.util.List;

public class GetUserResponseBody extends ClientUserIdRequestMetadata {
    @Serial
    private static final long serialVersionUID = -4359711467729294433L;

    private String clientUserName;
    private String bindingStatus;
    private List<String> authScope;
    private String identStatus;
    private String availableStatus;

    public GetUserResponseBody() {
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
}
