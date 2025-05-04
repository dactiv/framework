package com.github.dactiv.framework.fadada.domain.metadata.user;

import java.io.Serial;
import java.io.Serializable;

public class OpenUserIdRequestMetdata implements Serializable {
    @Serial
    private static final long serialVersionUID = -1347725221604256124L;

    private String openUserId;

    public OpenUserIdRequestMetdata(String openUserId) {
        this.openUserId = openUserId;
    }

    public OpenUserIdRequestMetdata() {
    }

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }
}
