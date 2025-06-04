package com.github.dactiv.framework.fasc.req.signtask;


import com.github.dactiv.framework.fasc.bean.common.OpenId;

import java.util.List;

public class GetSignTaskUrlReq extends SignTaskBaseReq {
    private OpenId initiator;

    private String redirectUrl;

    private List<String> nonEditableInfo;

    public OpenId getInitiator() {
        return initiator;
    }

    public void setInitiator(OpenId initiator) {
        this.initiator = initiator;
    }

    public List<String> getNonEditableInfo() {
        return nonEditableInfo;
    }

    public void setNonEditableInfo(List<String> nonEditableInfo) {
        this.nonEditableInfo = nonEditableInfo;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
