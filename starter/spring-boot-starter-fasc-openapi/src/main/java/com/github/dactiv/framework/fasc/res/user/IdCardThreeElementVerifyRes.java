package com.github.dactiv.framework.fasc.res.user;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

public class IdCardThreeElementVerifyRes extends BaseBean {
    private String serialNo;
    private String similarity;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }
}
