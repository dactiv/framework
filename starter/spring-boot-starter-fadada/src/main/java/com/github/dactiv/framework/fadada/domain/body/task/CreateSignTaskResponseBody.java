package com.github.dactiv.framework.fadada.domain.body.task;

import java.io.Serial;
import java.io.Serializable;

public class CreateSignTaskResponseBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -4660574049937937749L;
    private String signTaskId;

    public CreateSignTaskResponseBody() {
    }

    public String getSignTaskId() {
        return this.signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }
}
