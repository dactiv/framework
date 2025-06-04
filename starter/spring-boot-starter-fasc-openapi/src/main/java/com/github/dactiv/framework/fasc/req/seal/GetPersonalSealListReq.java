package com.github.dactiv.framework.fasc.req.seal;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @author zhoufucheng
 * @date 2022/11/30 15:43
 */
public class GetPersonalSealListReq extends BaseReq {
    private String openUserId;

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }
}
