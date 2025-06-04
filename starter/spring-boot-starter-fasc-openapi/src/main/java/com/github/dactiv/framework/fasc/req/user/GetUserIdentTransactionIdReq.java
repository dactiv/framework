package com.github.dactiv.framework.fasc.req.user;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class GetUserIdentTransactionIdReq extends BaseReq {

    private String openUserId;

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }
}