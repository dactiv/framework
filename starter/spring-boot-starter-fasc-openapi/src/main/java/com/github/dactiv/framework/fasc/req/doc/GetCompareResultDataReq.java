package com.github.dactiv.framework.fasc.req.doc;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

/**
 * @author zpt
 * @date 2023/6/28
 */
public class GetCompareResultDataReq extends BaseReq {
    private OpenId initiator;
    private String compareId;

    public OpenId getInitiator() {
        return initiator;
    }

    public void setInitiator(OpenId initiator) {
        this.initiator = initiator;
    }

    public String getCompareId() {
        return compareId;
    }

    public void setCompareId(String compareId) {
        this.compareId = compareId;
    }
}