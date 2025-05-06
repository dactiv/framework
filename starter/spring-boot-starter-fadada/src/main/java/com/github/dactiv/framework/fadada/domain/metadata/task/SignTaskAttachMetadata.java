package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;

public class SignTaskAttachMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -6732267493179009805L;

    private String attachId;
    private String attachName;

    public SignTaskAttachMetadata() {

    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }
}
