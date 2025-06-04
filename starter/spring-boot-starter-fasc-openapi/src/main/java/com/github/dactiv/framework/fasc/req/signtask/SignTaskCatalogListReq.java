package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

/**
 * @author hukc
 * @date 2022年10月31日
 */
public class SignTaskCatalogListReq extends BaseReq {
    private OpenId ownerId;

    public OpenId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenId ownerId) {
        this.ownerId = ownerId;
    }
}
