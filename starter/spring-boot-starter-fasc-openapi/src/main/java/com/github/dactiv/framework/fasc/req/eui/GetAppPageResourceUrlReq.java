package com.github.dactiv.framework.fasc.req.eui;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

/**
 * @author gongj
 * @date 2022/7/13
 */
public class GetAppPageResourceUrlReq extends BaseReq {
    private OpenId ownerId;
    private GetPageResourceUrlResource resource;

    public OpenId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenId ownerId) {
        this.ownerId = ownerId;
    }

    public GetPageResourceUrlResource getResource() {
        return resource;
    }

    public void setResource(GetPageResourceUrlResource resource) {
        this.resource = resource;
    }
}
