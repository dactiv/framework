package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SignTaskDetailActorMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -6304297105254342611L;

    private SignTaskDetailActorInfoMetadata actorInfo;
    private List<SignTaskFieldMetadata> signFields;
    private String joinStatus;
    private String joinTime;
    private String fillStatus;
    private String fillTime;
    private String signStatus;
    private String signTime;
    private Integer signOrderNo;
    private String blockStatus;
    private String actorNote;
    private String actorSignTaskUrl;
    private String actorSignTaskEmbedUrl;
    private String readStatus;
    private String readTime;

    public SignTaskDetailActorMetadata() {
    }

    public SignTaskDetailActorInfoMetadata getActorInfo() {
        return actorInfo;
    }

    public void setActorInfo(SignTaskDetailActorInfoMetadata actorInfo) {
        this.actorInfo = actorInfo;
    }

    public List<SignTaskFieldMetadata> getSignFields() {
        return signFields;
    }

    public void setSignFields(List<SignTaskFieldMetadata> signFields) {
        this.signFields = signFields;
    }

    public String getJoinStatus() {
        return joinStatus;
    }

    public void setJoinStatus(String joinStatus) {
        this.joinStatus = joinStatus;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getFillStatus() {
        return fillStatus;
    }

    public void setFillStatus(String fillStatus) {
        this.fillStatus = fillStatus;
    }

    public String getFillTime() {
        return fillTime;
    }

    public void setFillTime(String fillTime) {
        this.fillTime = fillTime;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public Integer getSignOrderNo() {
        return signOrderNo;
    }

    public void setSignOrderNo(Integer signOrderNo) {
        this.signOrderNo = signOrderNo;
    }

    public String getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(String blockStatus) {
        this.blockStatus = blockStatus;
    }

    public String getActorNote() {
        return actorNote;
    }

    public void setActorNote(String actorNote) {
        this.actorNote = actorNote;
    }

    public String getActorSignTaskUrl() {
        return actorSignTaskUrl;
    }

    public void setActorSignTaskUrl(String actorSignTaskUrl) {
        this.actorSignTaskUrl = actorSignTaskUrl;
    }

    public String getActorSignTaskEmbedUrl() {
        return actorSignTaskEmbedUrl;
    }

    public void setActorSignTaskEmbedUrl(String actorSignTaskEmbedUrl) {
        this.actorSignTaskEmbedUrl = actorSignTaskEmbedUrl;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}
