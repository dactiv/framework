package com.github.dactiv.framework.fasc.req.corp;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @author Fadada
 * @date 2021/12/6 10:20:27
 */
public class DisableCorpReq extends BaseReq {
    private String openCorpId;

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }
}
