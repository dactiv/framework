package com.github.dactiv.framework.fasc.res.corp;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

public class BusinessThreeElementVerifyRes extends BaseBean {
    private String serialNo;
    private String verifyResult;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(String verifyResult) {
        this.verifyResult = verifyResult;
    }
}
