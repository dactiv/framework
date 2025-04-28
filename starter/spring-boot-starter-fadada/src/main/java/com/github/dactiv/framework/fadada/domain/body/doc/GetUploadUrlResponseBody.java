package com.github.dactiv.framework.fadada.domain.body.doc;

import java.io.Serial;
import java.io.Serializable;

public class GetUploadUrlResponseBody implements Serializable {

    @Serial
    private static final long serialVersionUID = -3119913731916662240L;

    private String fddFileUrl;
    private String uploadUrl;

    public GetUploadUrlResponseBody() {
    }

    public String getFddFileUrl() {
        return this.fddFileUrl;
    }

    public String getUploadUrl() {
        return this.uploadUrl;
    }
}
