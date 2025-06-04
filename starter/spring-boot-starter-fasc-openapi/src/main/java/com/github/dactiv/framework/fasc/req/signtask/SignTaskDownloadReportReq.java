package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class SignTaskDownloadReportReq extends BaseReq {

    private String reportDownloadId;

    public String getReportDownloadId() {
        return reportDownloadId;
    }

    public void setReportDownloadId(String reportDownloadId) {
        this.reportDownloadId = reportDownloadId;
    }
}
