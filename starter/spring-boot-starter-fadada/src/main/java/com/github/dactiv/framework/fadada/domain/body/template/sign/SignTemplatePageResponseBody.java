package com.github.dactiv.framework.fadada.domain.body.template.sign;

import com.github.dactiv.framework.fadada.domain.body.PageableResponseBody;

import java.io.Serial;
import java.util.LinkedList;
import java.util.List;

public class SignTemplatePageResponseBody extends PageableResponseBody {

    @Serial
    private static final long serialVersionUID = -8510094573049565821L;

    private List<SignTemplateResponseBody> signTemplates = new LinkedList<>();

    public SignTemplatePageResponseBody() {
    }

    public List<SignTemplateResponseBody> getSignTemplates() {
        return signTemplates;
    }

    public void setSignTemplates(List<SignTemplateResponseBody> signTemplates) {
        this.signTemplates = signTemplates;
    }
}
