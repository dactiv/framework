package com.github.dactiv.framework.fadada.domain.body.task;

import com.github.dactiv.framework.fadada.domain.metadata.task.*;
import com.github.dactiv.framework.fadada.enumerate.SignTaskStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SignTaskDetailResponseBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 3329944198214194289L;
    private OpenIdMetadata initiator;
    private String initiatorMemberId;
    private String initiatorMemberName;
    private String signTaskId;
    private String signTaskSubject;
    private String storageType;
    private String signDocType;
    private SignTaskStatus signTaskStatus;
    private String createTime;
    private String signTaskSource;
    private String approvalStatus;
    private String rejectNote;
    private String businessTypeName;
    private String businessCode;
    private String templateId;
    private String startTime;
    private String finishTime;
    private String deadlineTime;
    private String terminationNote;
    private String certCAOrg;
    private Long businessTypeId;
    private String revokeReason;
    private String revokeNote;
    private Boolean autoFillFinalize;
    private Boolean autoFinish;
    private Boolean signInOrder;
    private String abolishedSignTaskId;
    private String originalSignTaskId;
    private String dueDate;
    private List<SignTaskDocMetadata> docs;
    private List<SignTaskAttachMetadata> attachs;
    private List<SignTaskDetailActorMetadata> actors;
    private List<WatermarkMetadata> watermarks;
    private List<ApprovalMetadata> approvalInfos;
    private String fileFormat;
    private String transReferenceId;

    public SignTaskDetailResponseBody() {
    }

    public OpenIdMetadata getInitiator() {
        return initiator;
    }

    public void setInitiator(OpenIdMetadata initiator) {
        this.initiator = initiator;
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

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }

    public String getSignTaskSubject() {
        return signTaskSubject;
    }

    public void setSignTaskSubject(String signTaskSubject) {
        this.signTaskSubject = signTaskSubject;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getSignDocType() {
        return signDocType;
    }

    public void setSignDocType(String signDocType) {
        this.signDocType = signDocType;
    }

    public SignTaskStatus getSignTaskStatus() {
        return signTaskStatus;
    }

    public void setSignTaskStatus(SignTaskStatus signTaskStatus) {
        this.signTaskStatus = signTaskStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSignTaskSource() {
        return signTaskSource;
    }

    public void setSignTaskSource(String signTaskSource) {
        this.signTaskSource = signTaskSource;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getTerminationNote() {
        return terminationNote;
    }

    public void setTerminationNote(String terminationNote) {
        this.terminationNote = terminationNote;
    }

    public String getCertCAOrg() {
        return certCAOrg;
    }

    public void setCertCAOrg(String certCAOrg) {
        this.certCAOrg = certCAOrg;
    }

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokeReason) {
        this.revokeReason = revokeReason;
    }

    public String getRevokeNote() {
        return revokeNote;
    }

    public void setRevokeNote(String revokeNote) {
        this.revokeNote = revokeNote;
    }

    public Boolean getAutoFillFinalize() {
        return autoFillFinalize;
    }

    public void setAutoFillFinalize(Boolean autoFillFinalize) {
        this.autoFillFinalize = autoFillFinalize;
    }

    public Boolean getAutoFinish() {
        return autoFinish;
    }

    public void setAutoFinish(Boolean autoFinish) {
        this.autoFinish = autoFinish;
    }

    public Boolean getSignInOrder() {
        return signInOrder;
    }

    public void setSignInOrder(Boolean signInOrder) {
        this.signInOrder = signInOrder;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public List<SignTaskDocMetadata> getDocs() {
        return docs;
    }

    public void setDocs(List<SignTaskDocMetadata> docs) {
        this.docs = docs;
    }

    public List<SignTaskAttachMetadata> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<SignTaskAttachMetadata> attachs) {
        this.attachs = attachs;
    }

    public List<SignTaskDetailActorMetadata> getActors() {
        return actors;
    }

    public void setActors(List<SignTaskDetailActorMetadata> actors) {
        this.actors = actors;
    }

    public List<WatermarkMetadata> getWatermarks() {
        return watermarks;
    }

    public void setWatermarks(List<WatermarkMetadata> watermarks) {
        this.watermarks = watermarks;
    }

    public List<ApprovalMetadata> getApprovalInfos() {
        return approvalInfos;
    }

    public void setApprovalInfos(List<ApprovalMetadata> approvalInfos) {
        this.approvalInfos = approvalInfos;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getTransReferenceId() {
        return transReferenceId;
    }

    public void setTransReferenceId(String transReferenceId) {
        this.transReferenceId = transReferenceId;
    }
}
