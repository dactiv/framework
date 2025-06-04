package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class GetDownloadEvidenceReportUrlReq extends BaseReq {
    private String signTaskId;

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }
}
