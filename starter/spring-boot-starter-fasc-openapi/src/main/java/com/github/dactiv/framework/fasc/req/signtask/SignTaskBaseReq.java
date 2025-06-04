package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @author Fadada
 * 2021/9/11 16:03:06
 */
public class SignTaskBaseReq extends BaseReq {
    private String signTaskId;

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }
}
