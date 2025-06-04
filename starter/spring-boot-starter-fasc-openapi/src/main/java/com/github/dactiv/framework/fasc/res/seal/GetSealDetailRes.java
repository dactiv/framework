package com.github.dactiv.framework.fasc.res.seal;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @Author： Fadada
 * @Date: 2022/10/8
 */
public class GetSealDetailRes extends BaseBean {

    private SealDetailInfo sealInfo;

    public SealDetailInfo getSealInfo() {
        return sealInfo;
    }

    public void setSealInfo(SealDetailInfo sealInfo) {
        this.sealInfo = sealInfo;
    }
}
