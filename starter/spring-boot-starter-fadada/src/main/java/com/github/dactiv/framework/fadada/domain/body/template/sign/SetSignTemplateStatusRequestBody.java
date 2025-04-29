package com.github.dactiv.framework.fadada.domain.body.template.sign;

import java.io.Serial;
import java.io.Serializable;

public class SetSignTemplateStatusRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 9207783970011177131L;

    private String openCorpId;
    private String signTemplateId;
    private String signTemplateStatus;

    public SetSignTemplateStatusRequestBody() {
    }

    public String getOpenCorpId() {
        return this.openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }

    public String getSignTemplateId() {
        return this.signTemplateId;
    }

    public void setSignTemplateId(String signTemplateId) {
        this.signTemplateId = signTemplateId;
    }

    public String getSignTemplateStatus() {
        return this.signTemplateStatus;
    }

    public void setSignTemplateStatus(String signTemplateStatus) {
        this.signTemplateStatus = signTemplateStatus;
    }
}
