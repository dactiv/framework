package com.github.dactiv.framework.fadada.domain.body.template;

import java.io.Serial;
import java.io.Serializable;

public class GetTemplateCreateUrlResponseBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 2798098383077025309L;

    private String templateCreateUrl;

    public GetTemplateCreateUrlResponseBody() {
    }

    public String getTemplateCreateUrl() {
        return templateCreateUrl;
    }

    public void setTemplateCreateUrl(String templateCreateUrl) {
        this.templateCreateUrl = templateCreateUrl;
    }
}
