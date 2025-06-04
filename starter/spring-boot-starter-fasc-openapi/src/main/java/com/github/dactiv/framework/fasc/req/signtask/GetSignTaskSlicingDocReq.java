package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

public class GetSignTaskSlicingDocReq extends BaseReq {

    private OpenId ownerId;

    private String signTaskId;

    public OpenId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenId ownerId) {
        this.ownerId = ownerId;
    }

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }
}