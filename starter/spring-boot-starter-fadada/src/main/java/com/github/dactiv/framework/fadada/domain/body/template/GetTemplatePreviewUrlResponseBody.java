package com.github.dactiv.framework.fadada.domain.body.template;

import java.io.Serial;
import java.io.Serializable;

public class GetTemplatePreviewUrlResponseBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -9178930163466731621L;

    private String templatePreviewUrl;

    public GetTemplatePreviewUrlResponseBody() {
    }

    public String getTemplatePreviewUrl() {
        return this.templatePreviewUrl;
    }

    public void setTemplatePreviewUrl(String templatePreviewUrl) {
        this.templatePreviewUrl = templatePreviewUrl;
    }
}
