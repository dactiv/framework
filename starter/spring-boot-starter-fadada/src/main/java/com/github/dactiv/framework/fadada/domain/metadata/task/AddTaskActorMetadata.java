package com.github.dactiv.framework.fadada.domain.metadata.task;

import com.github.dactiv.framework.fadada.domain.metadata.ActorMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.FillFieldMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.SignConfigMetadata;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class AddTaskActorMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 4241207881731870579L;

    private ActorMetadata actor;
    private List<FillFieldMetadata> fillFields;
    private List<AddTaskActorSignFieldMetadata> signFields;
    private SignConfigMetadata signConfigInfo;

    public AddTaskActorMetadata() {
    }

    public ActorMetadata getActor() {
        return actor;
    }

    public void setActor(ActorMetadata actor) {
        this.actor = actor;
    }

    public List<FillFieldMetadata> getFillFields() {
        return fillFields;
    }

    public void setFillFields(List<FillFieldMetadata> fillFields) {
        this.fillFields = fillFields;
    }

    public List<AddTaskActorSignFieldMetadata> getSignFields() {
        return signFields;
    }

    public void setSignFields(List<AddTaskActorSignFieldMetadata> signFields) {
        this.signFields = signFields;
    }

    public SignConfigMetadata getSignConfigInfo() {
        return signConfigInfo;
    }

    public void setSignConfigInfo(SignConfigMetadata signConfigInfo) {
        this.signConfigInfo = signConfigInfo;
    }
}
