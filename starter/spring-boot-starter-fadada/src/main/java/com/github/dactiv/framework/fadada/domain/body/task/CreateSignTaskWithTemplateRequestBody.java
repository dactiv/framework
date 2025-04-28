package com.github.dactiv.framework.fadada.domain.body.task;

import com.github.dactiv.framework.fadada.domain.metadata.task.AddTaskActorMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.task.WatermarkMetadata;

import java.io.Serial;
import java.util.List;

public class CreateSignTaskWithTemplateRequestBody extends CreateSignTaskRequestBody {

    @Serial
    private static final long serialVersionUID = 912124630836457L;

    private String signTemplateId;
    private String freeSignType;
    private List<AddTaskActorMetadata> actors;
    private List<WatermarkMetadata> watermarks;

    public CreateSignTaskWithTemplateRequestBody() {
    }

    public String getFreeSignType() {
        return this.freeSignType;
    }

    public void setFreeSignType(String freeSignType) {
        this.freeSignType = freeSignType;
    }

    public String getSignTemplateId() {
        return this.signTemplateId;
    }

    public void setSignTemplateId(String signTemplateId) {
        this.signTemplateId = signTemplateId;
    }

    public List<AddTaskActorMetadata> getActors() {
        return this.actors;
    }

    public void setActors(List<AddTaskActorMetadata> actors) {
        this.actors = actors;
    }

    public List<WatermarkMetadata> getWatermarks() {
        return this.watermarks;
    }

    public void setWatermarks(List<WatermarkMetadata> watermarks) {
        this.watermarks = watermarks;
    }
}
