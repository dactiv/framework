package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;

public class SignTaskActorMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -2879886221677914052L;

    private String actorName;
    private String actorId;

    public SignTaskActorMetadata() {
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }
}
