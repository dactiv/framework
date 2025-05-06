package com.github.dactiv.framework.fadada.domain.body.template.sign;

import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;

import java.io.Serial;
import java.io.Serializable;

public class SignTemplateDetailRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 2575511050014560748L;

    private OpenIdMetadata ownerId;
    private String signTemplateId;

    public SignTemplateDetailRequestBody() {
    }

    public SignTemplateDetailRequestBody(OpenIdMetadata ownerId) {
        this.ownerId = ownerId;
    }

    public SignTemplateDetailRequestBody(OpenIdMetadata ownerId, String signTemplateId) {
        this.ownerId = ownerId;
        this.signTemplateId = signTemplateId;
    }

    public OpenIdMetadata getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenIdMetadata ownerId) {
        this.ownerId = ownerId;
    }

    public String getSignTemplateId() {
        return this.signTemplateId;
    }

    public void setSignTemplateId(String signTemplateId) {
        this.signTemplateId = signTemplateId;
    }
}
