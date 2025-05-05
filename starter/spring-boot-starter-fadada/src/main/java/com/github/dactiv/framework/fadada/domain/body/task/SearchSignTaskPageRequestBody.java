package com.github.dactiv.framework.fadada.domain.body.task;

import com.github.dactiv.framework.fadada.domain.body.PageableRequestBody;
import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.task.SignTaskFilterMetadata;

import java.io.Serial;
import java.io.Serializable;

public class SearchSignTaskPageRequestBody extends PageableRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 1244797302436769191L;

    private OpenIdMetadata ownerId;
    private String ownerRole;
    private String pendingRole;
    private String memberId;
    private String catalogId;
    private SignTaskFilterMetadata listFilter;

    public SearchSignTaskPageRequestBody() {
    }

    public SearchSignTaskPageRequestBody(OpenIdMetadata ownerId, String ownerRole, String pendingRole, String memberId, String catalogId, SignTaskFilterMetadata listFilter) {
        this.ownerId = ownerId;
        this.ownerRole = ownerRole;
        this.pendingRole = pendingRole;
        this.memberId = memberId;
        this.catalogId = catalogId;
        this.listFilter = listFilter;
    }

    public OpenIdMetadata getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenIdMetadata ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerRole() {
        return ownerRole;
    }

    public void setOwnerRole(String ownerRole) {
        this.ownerRole = ownerRole;
    }

    public String getPendingRole() {
        return pendingRole;
    }

    public void setPendingRole(String pendingRole) {
        this.pendingRole = pendingRole;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public SignTaskFilterMetadata getListFilter() {
        return listFilter;
    }

    public void setListFilter(SignTaskFilterMetadata listFilter) {
        this.listFilter = listFilter;
    }
}
