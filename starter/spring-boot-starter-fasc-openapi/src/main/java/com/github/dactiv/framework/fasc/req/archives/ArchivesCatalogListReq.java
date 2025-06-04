package com.github.dactiv.framework.fasc.req.archives;


import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class ArchivesCatalogListReq extends BaseReq {

    private String openCorpId;

    private String catalogId;

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }
}
