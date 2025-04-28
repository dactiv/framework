package com.github.dactiv.framework.fadada.domain.body.task;

import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;

import java.io.Serial;
import java.io.Serializable;

public class CreateSignTaskRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 904622597934506675L;

    private OpenIdMetadata initiator;
    private String initiatorMemberId;
    private String initiatorEntityId;
    private String signTaskSubject;
    private String signDocType;
    private String businessCode;
    private Long businessTypeId;
    private String startApprovalFlowId;
    private String finalizeApprovalFlowId;
    private String expiresTime;
    private String dueDate;
    private String catalogId;
    private Boolean autoStart;
    private Boolean autoFinish;
    private Boolean autoFillFinalize;
    private String certCAOrg;
    private String businessId;
    private String transReferenceId;
    private String fileFormat;
    private Boolean offerCopies;
    private String callbackUrl;

    public CreateSignTaskRequestBody() {
    }

    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Boolean getOfferCopies() {
        return this.offerCopies;
    }

    public void setOfferCopies(Boolean offerCopies) {
        this.offerCopies = offerCopies;
    }

    public String getInitiatorEntityId() {
        return this.initiatorEntityId;
    }

    public void setInitiatorEntityId(String initiatorEntityId) {
        this.initiatorEntityId = initiatorEntityId;
    }

    public String getStartApprovalFlowId() {
        return this.startApprovalFlowId;
    }

    public void setStartApprovalFlowId(String startApprovalFlowId) {
        this.startApprovalFlowId = startApprovalFlowId;
    }

    public String getFinalizeApprovalFlowId() {
        return this.finalizeApprovalFlowId;
    }

    public void setFinalizeApprovalFlowId(String finalizeApprovalFlowId) {
        this.finalizeApprovalFlowId = finalizeApprovalFlowId;
    }

    public String getSignDocType() {
        return this.signDocType;
    }

    public void setSignDocType(String signDocType) {
        this.signDocType = signDocType;
    }

    public String getSignTaskSubject() {
        return this.signTaskSubject;
    }

    public void setSignTaskSubject(String signTaskSubject) {
        this.signTaskSubject = signTaskSubject;
    }

    public OpenIdMetadata getInitiator() {
        return this.initiator;
    }

    public void setInitiator(OpenIdMetadata initiator) {
        this.initiator = initiator;
    }

    public String getExpiresTime() {
        return this.expiresTime;
    }

    public void setExpiresTime(String expiresTime) {
        this.expiresTime = expiresTime;
    }

    public Boolean getAutoFillFinalize() {
        return this.autoFillFinalize;
    }

    public void setAutoFillFinalize(Boolean autoFillFinalize) {
        this.autoFillFinalize = autoFillFinalize;
    }

    public String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTransReferenceId() {
        return this.transReferenceId;
    }

    public void setTransReferenceId(String transReferenceId) {
        this.transReferenceId = transReferenceId;
    }

    public Boolean getAutoStart() {
        return this.autoStart;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    public String getCertCAOrg() {
        return this.certCAOrg;
    }

    public void setCertCAOrg(String certCAOrg) {
        this.certCAOrg = certCAOrg;
    }

    public Boolean getAutoFinish() {
        return this.autoFinish;
    }

    public void setAutoFinish(Boolean autoFinish) {
        this.autoFinish = autoFinish;
    }

    public String getCatalogId() {
        return this.catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public Long getBusinessTypeId() {
        return this.businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessCode() {
        return this.businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getInitiatorMemberId() {
        return this.initiatorMemberId;
    }

    public void setInitiatorMemberId(String initiatorMemberId) {
        this.initiatorMemberId = initiatorMemberId;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getFileFormat() {
        return this.fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
}
