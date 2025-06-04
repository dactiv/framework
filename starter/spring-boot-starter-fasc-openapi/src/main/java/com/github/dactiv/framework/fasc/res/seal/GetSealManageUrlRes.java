package com.github.dactiv.framework.fasc.res.seal;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @Author： Fadada
 * @Date: 2022/10/8
 */
public class GetSealManageUrlRes extends BaseBean {

    /**
     * 管理印章的页面链接
     */
    private String resourceUrl;

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
