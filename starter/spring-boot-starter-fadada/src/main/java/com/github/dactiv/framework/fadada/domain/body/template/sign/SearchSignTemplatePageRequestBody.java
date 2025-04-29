package com.github.dactiv.framework.fadada.domain.body.template.sign;

import com.github.dactiv.framework.fadada.domain.body.PageableRequestBody;
import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.template.SignTemplateFilterMetadata;

import java.io.Serial;

public class SearchSignTemplatePageRequestBody extends PageableRequestBody {

    @Serial
    private static final long serialVersionUID = 1665892028956520283L;
    private OpenIdMetadata ownerId;
    private SignTemplateFilterMetadata listFilter;

    public SearchSignTemplatePageRequestBody() {
    }

    public SearchSignTemplatePageRequestBody(OpenIdMetadata ownerId, SignTemplateFilterMetadata listFilter) {
        this.ownerId = ownerId;
        this.listFilter = listFilter;
    }

    public OpenIdMetadata getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenIdMetadata ownerId) {
        this.ownerId = ownerId;
    }

    public SignTemplateFilterMetadata getListFilter() {
        return listFilter;
    }

    public void setListFilter(SignTemplateFilterMetadata listFilter) {
        this.listFilter = listFilter;
    }
}
