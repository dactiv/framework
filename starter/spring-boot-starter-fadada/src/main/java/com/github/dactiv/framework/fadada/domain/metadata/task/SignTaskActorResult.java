package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;

public class SignTaskActorResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 5998997020592367790L;

    private String actorName;
    private String actorId;

    public SignTaskActorResult() {
    }

    public String getActorName() {
        return this.actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorId() {
        return this.actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }
}
