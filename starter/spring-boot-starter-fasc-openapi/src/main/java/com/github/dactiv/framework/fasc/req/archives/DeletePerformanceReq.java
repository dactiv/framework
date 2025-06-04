package com.github.dactiv.framework.fasc.req.archives;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class DeletePerformanceReq extends BaseReq {
    private String openCorpId;

    private String archivesId;

    private String performanceId;

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }

    public String getArchivesId() {
        return archivesId;
    }

    public void setArchivesId(String archivesId) {
        this.archivesId = archivesId;
    }

    public String getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(String performanceId) {
        this.performanceId = performanceId;
    }
}
