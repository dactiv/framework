package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @author gongj
 * @date 2022/7/12
 */
public class AddSignConfigTempInfo extends BaseBean {

    private Boolean blockHere;
    private Boolean requestVerifyFree;

    public Boolean getBlockHere() {
        return blockHere;
    }

    public void setBlockHere(Boolean blockHere) {
        this.blockHere = blockHere;
    }

    public Boolean getRequestVerifyFree() {
        return requestVerifyFree;
    }

    public void setRequestVerifyFree(Boolean requestVerifyFree) {
        this.requestVerifyFree = requestVerifyFree;
    }

}
