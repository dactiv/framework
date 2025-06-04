package com.github.dactiv.framework.fasc.req.voucher;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class GetVoucherTaskDetailReq extends BaseReq {
    private String signTaskId;

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }
}
