package com.github.dactiv.framework.fadada.domain.body.template.doc;

import java.io.Serial;
import java.io.Serializable;

public class CreateDocTemplateResponseBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -5443329004603112991L;
    private String docTemplateId;
    private String createSerialNo;

    public CreateDocTemplateResponseBody() {
    }

    public String getDocTemplateId() {
        return this.docTemplateId;
    }

    public void setDocTemplateId(String docTemplateId) {
        this.docTemplateId = docTemplateId;
    }

    public String getCreateSerialNo() {
        return this.createSerialNo;
    }

    public void setCreateSerialNo(String createSerialNo) {
        this.createSerialNo = createSerialNo;
    }
}
