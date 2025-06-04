package com.github.dactiv.framework.fasc.req.template;

import com.github.dactiv.framework.fasc.bean.base.BasePageReq;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

/**
 * @author Fadada
 * 2021/9/11 15:16:27
 */
public class GetDocTemplateListReq extends BasePageReq {
    private OpenId ownerId;
    private DocTemplateListFilterInfo listFilter;

    public OpenId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(OpenId ownerId) {
        this.ownerId = ownerId;
    }

    public DocTemplateListFilterInfo getListFilter() {
        return listFilter;
    }

    public void setListFilter(DocTemplateListFilterInfo listFilter) {
        this.listFilter = listFilter;
    }
}
