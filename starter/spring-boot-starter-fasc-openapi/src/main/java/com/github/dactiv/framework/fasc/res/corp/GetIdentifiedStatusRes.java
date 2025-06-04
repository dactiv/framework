package com.github.dactiv.framework.fasc.res.corp;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @author zfc
 * @date 2022年11月18日
 */
public class GetIdentifiedStatusRes extends BaseBean {
    private boolean identStatus;

    public boolean isIdentStatus() {
        return identStatus;
    }

    public void setIdentStatus(boolean identStatus) {
        this.identStatus = identStatus;
    }
}
