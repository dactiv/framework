package com.github.dactiv.framework.fasc.req.template;

import com.github.dactiv.framework.fasc.bean.base.BasePageReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

/**
 * @author Fadada
 * 2021/9/11 15:16:27
 */
public class GetSignTemplateListReq extends BasePageReq {

    private OpenId ownerId;
    private SignTemplateListFilterInfo listFilter;

    public OpenId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenId ownerId) {
        this.ownerId = ownerId;
    }

    public SignTemplateListFilterInfo getListFilter() {
        return listFilter;
    }

    public void setListFilter(SignTemplateListFilterInfo listFilter) {
        this.listFilter = listFilter;
    }
}
