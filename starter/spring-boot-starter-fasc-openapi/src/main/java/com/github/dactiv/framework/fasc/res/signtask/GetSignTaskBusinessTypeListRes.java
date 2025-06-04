package com.github.dactiv.framework.fasc.res.signtask;


import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class GetSignTaskBusinessTypeListRes extends BaseReq {

    private Long businessTypeId;
    private String businessTypeName;

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }
}
