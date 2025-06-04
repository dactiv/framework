package com.github.dactiv.framework.fasc.res.seal;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

import java.util.List;

/**
 * @author gongj
 * @date 2022/7/13
 */
public class GetSealListRes extends BaseBean {

    private List<GetListSealInfo> sealInfos;

    public List<GetListSealInfo> getSealInfos() {
        return sealInfos;
    }

    public void setSealInfos(List<GetListSealInfo> sealInfos) {
        this.sealInfos = sealInfos;
    }
}
