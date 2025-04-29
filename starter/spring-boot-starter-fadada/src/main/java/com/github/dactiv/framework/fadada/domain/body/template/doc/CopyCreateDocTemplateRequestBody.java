package com.github.dactiv.framework.fadada.domain.body.template.doc;

import java.io.Serial;
import java.io.Serializable;

public class CopyCreateDocTemplateRequestBody implements Serializable {

    @Serial
    private static final long serialVersionUID = -7664931162481837549L;

    private String openCorpId;
    private String docTemplateId;
    private String docTemplateName;
    private String createSerialNo;

    public CopyCreateDocTemplateRequestBody() {
    }

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
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

    public String getCreateSerialNo() {
        return createSerialNo;
    }

    public void setCreateSerialNo(String createSerialNo) {
        this.createSerialNo = createSerialNo;
    }
}
