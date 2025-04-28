package com.github.dactiv.framework.fadada.domain.body.template;

import java.io.Serial;
import java.io.Serializable;

public class GetTemplateEditUrlResponseBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 2047097137013859176L;

    private String templateEditUrl;

    public GetTemplateEditUrlResponseBody() {
    }

    public String getTemplateEditUrl() {
        return this.templateEditUrl;
    }

    public void setTemplateEditUrl(String templateEditUrl) {
        this.templateEditUrl = templateEditUrl;
    }
}
