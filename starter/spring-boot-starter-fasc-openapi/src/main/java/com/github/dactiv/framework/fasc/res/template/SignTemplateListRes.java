package com.github.dactiv.framework.fasc.res.template;

import com.github.dactiv.framework.fasc.bean.base.BasePageRes;

import java.util.List;

/**
 * @author Fadada
 * 2021/9/11 15:16:27
 */
public class SignTemplateListRes extends BasePageRes {
    private List<SignTemplateListInfo> signTemplates;

    public List<SignTemplateListInfo> getSignTemplates() {
        return signTemplates;
    }

    public void setSignTemplates(List<SignTemplateListInfo> signTemplates) {
        this.signTemplates = signTemplates;
    }
}
