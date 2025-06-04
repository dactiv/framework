package com.github.dactiv.framework.fasc.req.template;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class CancelPersonalSealFreeSignReq extends BaseReq {
    private String openUserId;
    private Long sealId;
    private String businessId;

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }

    public Long getSealId() {
        return sealId;
    }

    public void setSealId(Long sealId) {
        this.sealId = sealId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}