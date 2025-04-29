package com.github.dactiv.framework.fadada.domain.body.template.sign;

import java.io.Serial;
import java.io.Serializable;

public class CopyCreateSignTemplateRequestBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 4907900574447247481L;
    private String openCorpId;
    private String signTemplateId;
    private String signTemplateName;
    private String createSerialNo;

    public CopyCreateSignTemplateRequestBody() {
    }

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
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

    public String getCreateSerialNo() {
        return createSerialNo;
    }

    public void setCreateSerialNo(String createSerialNo) {
        this.createSerialNo = createSerialNo;
    }
}
