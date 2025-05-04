package com.github.dactiv.framework.fadada.domain.body.task;

import com.github.dactiv.framework.fadada.domain.metadata.task.SignTaskIdMetadata;

import java.io.Serial;

public class GetSignTaskActorUrlRequestBody extends SignTaskIdMetadata {
    @Serial
    private static final long serialVersionUID = 1602595785045087805L;
    private String actorId;
    private String clientUserId;
    private String redirectMiniAppUrl;
    private String redirectUrl;

    public GetSignTaskActorUrlRequestBody() {
    }

    public GetSignTaskActorUrlRequestBody(String signTaskId, String actorId) {
        super(signTaskId);
        this.actorId = actorId;
    }

    public String getActorId() {
        return this.actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getClientUserId() {
        return this.clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectMiniAppUrl() {
        return this.redirectMiniAppUrl;
    }

    public void setRedirectMiniAppUrl(String redirectMiniAppUrl) {
        this.redirectMiniAppUrl = redirectMiniAppUrl;
    }
}
