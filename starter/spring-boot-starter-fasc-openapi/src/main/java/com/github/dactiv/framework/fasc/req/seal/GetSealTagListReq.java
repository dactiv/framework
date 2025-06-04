package com.github.dactiv.framework.fasc.req.seal;


import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @Author： zpt
 * @Date: 2023/6/26
 */
public class GetSealTagListReq extends BaseReq {

    private String openCorpId;

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }
}