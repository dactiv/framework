package com.github.dactiv.framework.fadada.domain.body.template.doc;

import java.io.Serial;
import java.io.Serializable;

public class DeleteDocTemplateRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -2590273084400511167L;

    private String docTemplateId;
    private String openCorpId;

    public DeleteDocTemplateRequestBody() {
    }

    public String getDocTemplateId() {
        return docTemplateId;
    }

    public void setDocTemplateId(String docTemplateId) {
        this.docTemplateId = docTemplateId;
    }

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }
}
