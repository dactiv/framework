package com.github.dactiv.framework.fasc.req.user;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class MainlandPermitOcrReq extends BaseReq {
    private String imageBase64;

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
