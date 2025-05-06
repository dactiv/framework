package com.github.dactiv.framework.fadada.domain.metadata.task;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SignTaskDetailActorInfoMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -5411873408601266343L;

    private String actorId;
    private String actorType;
    private String actorName;
    private String actorOpenId;
    private List<String> permissions;
    @JsonProperty("isInitiator")
    private Boolean isInitiator;

    public SignTaskDetailActorInfoMetadata() {
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getActorType() {
        return actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorOpenId() {
        return actorOpenId;
    }

    public void setActorOpenId(String actorOpenId) {
        this.actorOpenId = actorOpenId;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public Boolean getInitiator() {
        return isInitiator;
    }

    public void setInitiator(Boolean initiator) {
        isInitiator = initiator;
    }
}
