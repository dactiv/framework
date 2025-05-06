package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;

public class ApprovalMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -9106444434018693923L;
    private String approvalFlowId;
    private String approvalId;
    private String approvalType;
    private String approvalStatus;
    private String rejectNote;

    public ApprovalMetadata() {
    }

    public String getApprovalFlowId() {
        return approvalFlowId;
    }

    public void setApprovalFlowId(String approvalFlowId) {
        this.approvalFlowId = approvalFlowId;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
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
}
