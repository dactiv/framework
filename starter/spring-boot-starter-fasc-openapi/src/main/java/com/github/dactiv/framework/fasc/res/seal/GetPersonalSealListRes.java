package com.github.dactiv.framework.fasc.res.seal;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

import java.util.List;

/**
 * @author zhoufucheng
 * @date 2022/11/30 15:47
 */
public class GetPersonalSealListRes extends BaseBean {
    private List<PersonalSealInfo> sealInfos;

    public List<PersonalSealInfo> getSealInfos() {
        return sealInfos;
    }

    public void setSealInfos(List<PersonalSealInfo> sealInfos) {
        this.sealInfos = sealInfos;
    }
}
