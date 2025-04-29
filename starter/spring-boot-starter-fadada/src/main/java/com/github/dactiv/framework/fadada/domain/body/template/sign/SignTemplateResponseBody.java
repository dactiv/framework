package com.github.dactiv.framework.fadada.domain.body.template.sign;

import java.io.Serial;
import java.io.Serializable;

public class SignTemplateResponseBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 2734365675383149607L;

    private String signTemplateId;
    private String signTemplateName;
    private String businessTypeName;
    private String signTemplateStatus;
    private String createTime;
    private String updateTime;
    private String catalogName;
    private String creatorMemberName;
    private String createSerialNo;
    private String storageType;
    private String templateVersion;
    private String description;
    private String signTemplateType;

    public SignTemplateResponseBody() {
    }

    public String getSignTemplateId() {
        return signTemplateId;
    }

    public void setSignTemplateId(String signTemplateId) {
        this.signTemplateId = signTemplateId;
    }

    public String getSignTemplateName() {
        return signTemplateName;
    }

    public void setSignTemplateName(String signTemplateName) {
        this.signTemplateName = signTemplateName;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getSignTemplateStatus() {
        return signTemplateStatus;
    }

    public void setSignTemplateStatus(String signTemplateStatus) {
        this.signTemplateStatus = signTemplateStatus;
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

    public String getCreatorMemberName() {
        return creatorMemberName;
    }

    public void setCreatorMemberName(String creatorMemberName) {
        this.creatorMemberName = creatorMemberName;
    }

    public String getCreateSerialNo() {
        return createSerialNo;
    }

    public void setCreateSerialNo(String createSerialNo) {
        this.createSerialNo = createSerialNo;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(String templateVersion) {
        this.templateVersion = templateVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSignTemplateType() {
        return signTemplateType;
    }

    public void setSignTemplateType(String signTemplateType) {
        this.signTemplateType = signTemplateType;
    }
}
