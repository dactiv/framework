package com.github.dactiv.framework.fasc.req.template;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @author Fadada
 * @date 2023/6/27 17:20
 */
public class GetCorpFieldListReq  extends BaseReq {

    private String openCorpId;

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }
}
