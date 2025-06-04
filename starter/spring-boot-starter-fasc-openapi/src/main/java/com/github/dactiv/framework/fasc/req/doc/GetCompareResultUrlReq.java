package com.github.dactiv.framework.fasc.req.doc;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

/**
 * @author zhoufucheng
 * @date 2022/12/28 0028 15:53
 */
public class GetCompareResultUrlReq extends BaseReq {
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
