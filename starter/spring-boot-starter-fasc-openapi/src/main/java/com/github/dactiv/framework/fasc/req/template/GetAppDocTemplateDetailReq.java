package com.github.dactiv.framework.fasc.req.template;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @author zhoufucheng
 * @date 2022/12/25 0025 19:18
 */
public class GetAppDocTemplateDetailReq extends BaseReq {
    private String appDocTemplateId;

    public String getAppDocTemplateId() {
        return appDocTemplateId;
    }

    public void setAppDocTemplateId(String appDocTemplateId) {
        this.appDocTemplateId = appDocTemplateId;
    }
}
