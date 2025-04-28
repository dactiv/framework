package com.github.dactiv.framework.fadada.domain.body.user;

import java.io.Serial;

public class GetUserAutStatusRequestBody extends GetUserRequestBody {
    @Serial
    private static final long serialVersionUID = -6062366225817140647L;

    private String clientUserId;

    public GetUserAutStatusRequestBody() {
    }

    public GetUserAutStatusRequestBody(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }
}
