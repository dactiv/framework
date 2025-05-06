package com.github.dactiv.framework.fadada.domain.body.task;

import com.github.dactiv.framework.fadada.domain.metadata.task.SignTaskActorMetadata;
import com.github.dactiv.framework.fadada.enumerate.SignTaskStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SignTaskResponseBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 5170819334835973306L;

    private String signTaskId;
    private String transReferenceId;
    private String signTaskSubject;
    private SignTaskStatus signTaskStatus;
    private String initiatorName;
    private String initiatorMemberId;
    private String initiatorMemberName;
    private List<SignTaskActorMetadata> actorResults;
    private String actorName;
    private String createTime;
    private String finishTime;
    private String catalogName;
    private Long catalogId;
    private String deadlineTime;
    private String startTime;
    private String templateId;
    private String businessCode;
    private Long businessTypeId;
    private String businessTypeName;
    private String rejectNote;
    private String approvalStatus;
    private String signTaskSource;
    private String terminationNote;
    private String abolishedSignTaskId;
    private String originalSignTaskId;
    private String revokeReason;
    private String dueDate;
    private String storageType;

    public SignTaskResponseBody() {
    }

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }

    public String getTransReferenceId() {
        return transReferenceId;
    }

    public void setTransReferenceId(String transReferenceId) {
        this.transReferenceId = transReferenceId;
    }

    public String getSignTaskSubject() {
        return signTaskSubject;
    }

    public void setSignTaskSubject(String signTaskSubject) {
        this.signTaskSubject = signTaskSubject;
    }

    public SignTaskStatus getSignTaskStatus() {
        return signTaskStatus;
    }

    public void setSignTaskStatus(SignTaskStatus signTaskStatus) {
        this.signTaskStatus = signTaskStatus;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getInitiatorMemberId() {
        return initiatorMemberId;
    }

    public void setInitiatorMemberId(String initiatorMemberId) {
        this.initiatorMemberId = initiatorMemberId;
    }

    public String getInitiatorMemberName() {
        return initiatorMemberName;
    }

    public void setInitiatorMemberName(String initiatorMemberName) {
        this.initiatorMemberName = initiatorMemberName;
    }

    public List<SignTaskActorMetadata> getActorResults() {
        return actorResults;
    }

    public void setActorResults(List<SignTaskActorMetadata> actorResults) {
        this.actorResults = actorResults;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getSignTaskSource() {
        return signTaskSource;
    }

    public void setSignTaskSource(String signTaskSource) {
        this.signTaskSource = signTaskSource;
    }

    public String getTerminationNote() {
        return terminationNote;
    }

    public void setTerminationNote(String terminationNote) {
        this.terminationNote = terminationNote;
    }

    public String getAbolishedSignTaskId() {
        return abolishedSignTaskId;
    }

    public void setAbolishedSignTaskId(String abolishedSignTaskId) {
        this.abolishedSignTaskId = abolishedSignTaskId;
    }

    public String getOriginalSignTaskId() {
        return originalSignTaskId;
    }

    public void setOriginalSignTaskId(String originalSignTaskId) {
        this.originalSignTaskId = originalSignTaskId;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokeReason) {
        this.revokeReason = revokeReason;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
}
