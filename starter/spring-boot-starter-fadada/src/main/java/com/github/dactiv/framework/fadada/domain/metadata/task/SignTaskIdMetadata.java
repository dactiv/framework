package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;

public class SignTaskIdMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 6588938705146554509L;

    private String signTaskId;

    public SignTaskIdMetadata() {

    }

    public SignTaskIdMetadata(String signTaskId) {
        this.signTaskId = signTaskId;
    }

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }
}
