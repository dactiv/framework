package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

public class ActorAttachMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 9151176585480110888L;

    private String actorAttachName;
    private Boolean required = false;

    public ActorAttachMetadata() {
    }

    public String getActorAttachName() {
        return this.actorAttachName;
    }

    public void setActorAttachName(String actorAttachName) {
        this.actorAttachName = actorAttachName;
    }

    public Boolean getRequired() {
        return this.required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}
