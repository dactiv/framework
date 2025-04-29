package com.github.dactiv.framework.fadada.domain.metadata.template;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SignTemplateFilterMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -6387297273836634025L;

    private String signTemplateName;
    private List<String> signTemplateStatus;

    public SignTemplateFilterMetadata() {
    }

    public String getSignTemplateName() {
        return signTemplateName;
    }

    public void setSignTemplateName(String signTemplateName) {
        this.signTemplateName = signTemplateName;
    }

    public List<String> getSignTemplateStatus() {
        return signTemplateStatus;
    }

    public void setSignTemplateStatus(List<String> signTemplateStatus) {
        this.signTemplateStatus = signTemplateStatus;
    }
}
