package com.github.dactiv.framework.fasc.req.template;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @author zhoufucheng
 * @date 2022/12/25 0025 19:29
 */
public class GetAppSignTemplateDetailReq extends BaseReq {
    private String appSignTemplateId;

    public String getAppSignTemplateId() {
        return appSignTemplateId;
    }

    public void setAppSignTemplateId(String appSignTemplateId) {
        this.appSignTemplateId = appSignTemplateId;
    }
}
