package com.github.dactiv.framework.fasc.req.archives;


import com.github.dactiv.framework.fasc.bean.base.BasePageReq;

public class ArchivedListReq extends BasePageReq {

    private String openCorpId;

    private String contractType;

    private String signTaskId;

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }
}
