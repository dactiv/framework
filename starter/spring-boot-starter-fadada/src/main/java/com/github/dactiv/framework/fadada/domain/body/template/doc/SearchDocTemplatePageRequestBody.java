package com.github.dactiv.framework.fadada.domain.body.template.doc;

import com.github.dactiv.framework.fadada.domain.body.PageableRequestBody;
import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.template.DocTemplateFilterMetadata;

import java.io.Serial;

public class SearchDocTemplatePageRequestBody extends PageableRequestBody {

    @Serial
    private static final long serialVersionUID = -7396308806461204816L;

    private OpenIdMetadata ownerId;
    private DocTemplateFilterMetadata listFilter;

    public SearchDocTemplatePageRequestBody() {
    }


    public SearchDocTemplatePageRequestBody(OpenIdMetadata ownerId, DocTemplateFilterMetadata listFilter) {
        this.ownerId = ownerId;
        this.listFilter = listFilter;
    }

    public OpenIdMetadata getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenIdMetadata ownerId) {
        this.ownerId = ownerId;
    }

    public DocTemplateFilterMetadata getListFilter() {
        return listFilter;
    }

    public void setListFilter(DocTemplateFilterMetadata listFilter) {
        this.listFilter = listFilter;
    }
}
