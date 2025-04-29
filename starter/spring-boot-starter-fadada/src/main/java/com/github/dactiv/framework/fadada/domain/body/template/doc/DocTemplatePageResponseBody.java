package com.github.dactiv.framework.fadada.domain.body.template.doc;

import com.github.dactiv.framework.fadada.domain.body.PageableResponseBody;

import java.io.Serial;
import java.util.LinkedList;
import java.util.List;

public class DocTemplatePageResponseBody extends PageableResponseBody {

    @Serial
    private static final long serialVersionUID = -8510094573049565821L;

    private List<DocTemplateResponseBody> docTemplates = new LinkedList<>();

    public DocTemplatePageResponseBody() {
    }

    public List<DocTemplateResponseBody> getDocTemplates() {
        return docTemplates;
    }

    public void setDocTemplates(List<DocTemplateResponseBody> docTemplates) {
        this.docTemplates = docTemplates;
    }
}
