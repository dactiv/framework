package com.github.dactiv.framework.fadada.domain.body.template.sign;

import java.io.Serial;
import java.io.Serializable;

public class DeleteSignTemplateRequestBody implements Serializable {

    @Serial
    private static final long serialVersionUID = -6100092834298009727L;

    private String signTemplateId;

    private String openCorpId;

    public DeleteSignTemplateRequestBody() {
    }

    public String getSignTemplateId() {
        return signTemplateId;
    }

    public void setSignTemplateId(String signTemplateId) {
        this.signTemplateId = signTemplateId;
    }

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }
}
