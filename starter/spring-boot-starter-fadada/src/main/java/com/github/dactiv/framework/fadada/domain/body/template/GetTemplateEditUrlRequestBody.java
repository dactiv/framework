package com.github.dactiv.framework.fadada.domain.body.template;

import java.io.Serial;
import java.io.Serializable;

public class GetTemplateEditUrlRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -9198321268722343936L;
    private String openCorpId;
    private String templateId;
    private String redirectUrl;

    public GetTemplateEditUrlRequestBody() {
    }

    public String getOpenCorpId() {
        return this.openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
