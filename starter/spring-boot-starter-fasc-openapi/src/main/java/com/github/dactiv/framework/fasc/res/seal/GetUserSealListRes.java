package com.github.dactiv.framework.fasc.res.seal;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

import java.util.List;

/**
 * @Author： Fadada
 * @Date: 2022/10/8
 */
public class GetUserSealListRes extends BaseBean {

    private List<GetUserListSealInfo> sealInfos;

    public List<GetUserListSealInfo> getSealInfos() {
        return sealInfos;
    }

    public void setSealInfos(List<GetUserListSealInfo> sealInfos) {
        this.sealInfos = sealInfos;
    }
}
