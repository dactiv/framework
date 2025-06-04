package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class GetSignTaskPicDocTicketReq extends BaseReq {

    private String slicingTicketId;

    public String getSlicingTicketId() {
        return slicingTicketId;
    }

    public void setSlicingTicketId(String slicingTicketId) {
        this.slicingTicketId = slicingTicketId;
    }
}