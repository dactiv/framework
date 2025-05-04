package com.github.dactiv.framework.fadada.domain.metadata.user;

import java.io.Serial;

public class ClientUserIdRequestMetadata extends OpenUserIdRequestMetdata {

    @Serial
    private static final long serialVersionUID = -6062366225817140647L;

    private String clientUserId;

    public ClientUserIdRequestMetadata() {
    }

    public ClientUserIdRequestMetadata(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public ClientUserIdRequestMetadata(String openUserId, String clientUserId) {
        super(openUserId);
        this.clientUserId = clientUserId;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }
}
