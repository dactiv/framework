package com.github.dactiv.framework.fadada.domain.body.template.doc;

import java.io.Serial;
import java.io.Serializable;

public class CreateDocTemplateRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -4035617440160728121L;

    private String openCorpId;
    private String docTemplateName;
    private String createSerialNo;
    private String fileId;

    public CreateDocTemplateRequestBody() {
    }

    public String getOpenCorpId() {
        return this.openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }

    public String getDocTemplateName() {
        return this.docTemplateName;
    }

    public void setDocTemplateName(String docTemplateName) {
        this.docTemplateName = docTemplateName;
    }

    public String getCreateSerialNo() {
        return this.createSerialNo;
    }

    public void setCreateSerialNo(String createSerialNo) {
        this.createSerialNo = createSerialNo;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
