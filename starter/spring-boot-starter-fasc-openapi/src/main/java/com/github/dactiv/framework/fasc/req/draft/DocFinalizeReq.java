package com.github.dactiv.framework.fasc.req.draft;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @author zhoufucheng
 * @date 2023/11/30 13:55
 */
public class DocFinalizeReq extends BaseReq {
    private String contractConsultId;

    public String getContractConsultId() {
        return contractConsultId;
    }

    public void setContractConsultId(String contractConsultId) {
        this.contractConsultId = contractConsultId;
    }
}
