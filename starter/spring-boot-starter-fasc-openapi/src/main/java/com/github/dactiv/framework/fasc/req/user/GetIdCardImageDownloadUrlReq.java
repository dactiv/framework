package com.github.dactiv.framework.fasc.req.user;


import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class GetIdCardImageDownloadUrlReq extends BaseReq {

    private String verifyId ;

    public String getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(String verifyId) {
        this.verifyId = verifyId;
    }
}