package com.github.dactiv.framework.fasc.req.doc;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

public class GetExamineDataReq extends BaseReq {

    private OpenId initiator;

    private String examineId;

    public OpenId getInitiator() {
        return initiator;
    }

    public void setInitiator(OpenId initiator) {
        this.initiator = initiator;
    }

    public String getExamineId() {
        return examineId;
    }

    public void setExamineId(String examineId) {
        this.examineId = examineId;
    }
}