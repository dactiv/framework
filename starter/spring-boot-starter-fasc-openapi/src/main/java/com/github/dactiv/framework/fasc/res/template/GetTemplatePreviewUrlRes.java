package com.github.dactiv.framework.fasc.res.template;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @author Fadada
 * @date 2022/11/3 11:07:53
 */
public class GetTemplatePreviewUrlRes extends BaseBean {

    private String templatePreviewUrl;

    public String getTemplatePreviewUrl() {
        return templatePreviewUrl;
    }

    public void setTemplatePreviewUrl(String templatePreviewUrl) {
        this.templatePreviewUrl = templatePreviewUrl;
    }
}
