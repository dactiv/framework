package com.github.dactiv.framework.fadada.domain.body.template.sign;

import java.io.Serial;
import java.io.Serializable;

public class CopyCreateSignTemplateResponseBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 3364639096962248654L;

    private String signTemplateId;
    private String signTemplateName;
    private String createSerialNo;

    public CopyCreateSignTemplateResponseBody() {
    }

    public String getSignTemplateId() {
        return this.signTemplateId;
    }

    public void setSignTemplateId(String signTemplateId) {
        this.signTemplateId = signTemplateId;
    }

    public String getSignTemplateName() {
        return this.signTemplateName;
    }

    public void setSignTemplateName(String signTemplateName) {
        this.signTemplateName = signTemplateName;
    }

    public String getCreateSerialNo() {
        return this.createSerialNo;
    }

    public void setCreateSerialNo(String createSerialNo) {
        this.createSerialNo = createSerialNo;
    }
}
