package com.github.dactiv.framework.fadada.domain.body.template.doc;

import java.io.Serial;
import java.io.Serializable;

public class SetDocTemplateStatusRequestBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 9207783970011177131L;

    private String openCorpId;
    private String docTemplateId;
    private String docTemplateStatus;

    public SetDocTemplateStatusRequestBody() {
    }

    public String getOpenCorpId() {
        return this.openCorpId;
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

    public String getDocTemplateStatus() {
        return docTemplateStatus;
    }

    public void setDocTemplateStatus(String docTemplateStatus) {
        this.docTemplateStatus = docTemplateStatus;
    }
}
