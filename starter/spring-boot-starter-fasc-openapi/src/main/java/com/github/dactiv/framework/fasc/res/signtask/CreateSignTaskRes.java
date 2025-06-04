package com.github.dactiv.framework.fasc.res.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @author Fadada
 * 2021/9/13 16:28:57
 */
public class CreateSignTaskRes extends BaseBean {
    private String signTaskId;

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }
}
