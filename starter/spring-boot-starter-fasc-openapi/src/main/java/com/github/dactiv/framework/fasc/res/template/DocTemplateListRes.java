package com.github.dactiv.framework.fasc.res.template;

import com.github.dactiv.framework.fasc.bean.base.BasePageRes;

import java.util.List;

/**
 * @author Fadada
 * 2021/9/11 15:16:27
 */
public class DocTemplateListRes extends BasePageRes {
    private List<DocTemplateListInfo> docTemplates;

    public List<DocTemplateListInfo> getDocTemplates() {
        return docTemplates;
    }

    public void setDocTemplates(List<DocTemplateListInfo> docTemplates) {
        this.docTemplates = docTemplates;
    }
}
