package com.github.dactiv.framework.fadada.domain.metadata.template;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class DocTemplateFilterMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 8848474299719339446L;
    private String docTemplateName;
    private List<String> docTemplateStatus;

    public DocTemplateFilterMetadata() {

    }

    public String getDocTemplateName() {
        return docTemplateName;
    }

    public void setDocTemplateName(String docTemplateName) {
        this.docTemplateName = docTemplateName;
    }

    public List<String> getDocTemplateStatus() {
        return docTemplateStatus;
    }

    public void setDocTemplateStatus(List<String> docTemplateStatus) {
        this.docTemplateStatus = docTemplateStatus;
    }
}
