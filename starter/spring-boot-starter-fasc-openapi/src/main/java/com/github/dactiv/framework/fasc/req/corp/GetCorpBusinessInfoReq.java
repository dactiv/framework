package com.github.dactiv.framework.fasc.req.corp;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class GetCorpBusinessInfoReq extends BaseReq {
    private String corpName;
    private String corpIdentNo;

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpIdentNo() {
        return corpIdentNo;
    }

    public void setCorpIdentNo(String corpIdentNo) {
        this.corpIdentNo = corpIdentNo;
    }
}
