package com.github.dactiv.framework.fadada.domain.body.template.doc;

import java.io.Serial;
import java.io.Serializable;

public class DocTemplateResponseBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 2734365675383149607L;

    private String docTemplateId;
    private String docTemplateName;
    private String docTemplateStatus;
    private String createTime;
    private String updateTime;
    private String catalogName;
    private String createSerialNo;
    private String creatorMemberName;
    private String storageType;

    public DocTemplateResponseBody() {
    }

    public String getDocTemplateId() {
        return docTemplateId;
    }

    public void setDocTemplateId(String docTemplateId) {
        this.docTemplateId = docTemplateId;
    }

    public String getDocTemplateName() {
        return docTemplateName;
    }

    public void setDocTemplateName(String docTemplateName) {
        this.docTemplateName = docTemplateName;
    }

    public String getDocTemplateStatus() {
        return docTemplateStatus;
    }

    public void setDocTemplateStatus(String docTemplateStatus) {
        this.docTemplateStatus = docTemplateStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCreateSerialNo() {
        return createSerialNo;
    }

    public void setCreateSerialNo(String createSerialNo) {
        this.createSerialNo = createSerialNo;
    }

    public String getCreatorMemberName() {
        return creatorMemberName;
    }

    public void setCreatorMemberName(String creatorMemberName) {
        this.creatorMemberName = creatorMemberName;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
}
