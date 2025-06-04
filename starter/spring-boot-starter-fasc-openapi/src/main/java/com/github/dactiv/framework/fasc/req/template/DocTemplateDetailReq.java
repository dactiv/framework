package com.github.dactiv.framework.fasc.req.template;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

/**
 * @author Fadada
 * 2021/9/11 15:16:27
 */
public class DocTemplateDetailReq extends BaseReq {
    private OpenId ownerId;
    private String docTemplateId;

    public String getDocTemplateId() {
        return docTemplateId;
    }

    public void setDocTemplateId(String docTemplateId) {
        this.docTemplateId = docTemplateId;
    }

    public OpenId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenId ownerId) {
        this.ownerId = ownerId;
    }
}
