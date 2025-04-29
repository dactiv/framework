package com.github.dactiv.framework.fadada.domain.metadata.task;

public class OpenIdMetadata {

    private String idType;
    private String openId;

    public OpenIdMetadata() {
    }

    public String getIdType() {
        return this.idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public static OpenIdMetadata getInstance(String idType, String id) {
        OpenIdMetadata openId = new OpenIdMetadata();
        openId.setIdType(idType);
        openId.setOpenId(id);
        return openId;
    }
}
