package com.github.dactiv.framework.fadada.domain.body;

import java.io.Serial;
import java.io.Serializable;

public class HttpResponseBody implements Serializable    {
    @Serial
    private static final long serialVersionUID = 2633236591580332321L;

    private Integer httpStatusCode;
    private String requestId;

    public HttpResponseBody() {
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
