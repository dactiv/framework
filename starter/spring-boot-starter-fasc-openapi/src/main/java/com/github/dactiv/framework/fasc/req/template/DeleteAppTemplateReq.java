package com.github.dactiv.framework.fasc.req.template;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class DeleteAppTemplateReq extends BaseReq {

    private String appDocTemplateId;

    public String getAppDocTemplateId() {
        return appDocTemplateId;
    }

    public void setAppDocTemplateId(String appDocTemplateId) {
        this.appDocTemplateId = appDocTemplateId;
    }
}
