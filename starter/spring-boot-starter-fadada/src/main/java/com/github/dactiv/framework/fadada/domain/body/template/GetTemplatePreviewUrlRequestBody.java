package com.github.dactiv.framework.fadada.domain.body.template;

import java.io.Serial;
import java.io.Serializable;

public class GetTemplatePreviewUrlRequestBody implements Serializable {

    @Serial
    private static final long serialVersionUID = -7677744802485001202L;

    private String openCorpId;
    private String templateId;
    private String redirectUrl;

    public GetTemplatePreviewUrlRequestBody() {
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
