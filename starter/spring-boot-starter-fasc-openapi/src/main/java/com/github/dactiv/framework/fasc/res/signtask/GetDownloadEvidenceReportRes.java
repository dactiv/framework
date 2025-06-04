package com.github.dactiv.framework.fasc.res.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

public class GetDownloadEvidenceReportRes extends BaseBean {
    private String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
