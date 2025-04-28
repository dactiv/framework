package com.github.dactiv.framework.fadada.domain.body.user;

import java.io.Serial;
import java.io.Serializable;

public class GetUserRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -1347725221604256124L;

    private String openUserId;

    public GetUserRequestBody(String openUserId) {
        this.openUserId = openUserId;
    }

    public GetUserRequestBody() {
    }

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }
}
