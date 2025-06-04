package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

import java.util.List;

public class SignTaskPreFillUrlReq extends BaseReq {

    private String signTaskId;

    private String redirectUrl;

    private List<String> nonEditableInfo;

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public List<String> getNonEditableInfo() {
        return nonEditableInfo;
    }

    public void setNonEditableInfo(List<String> nonEditableInfo) {
        this.nonEditableInfo = nonEditableInfo;
    }
}
