package com.github.dactiv.framework.fasc.req.corp;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class CorpUnbindReq extends BaseReq {
    
    private String openCorpId;

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }
}
