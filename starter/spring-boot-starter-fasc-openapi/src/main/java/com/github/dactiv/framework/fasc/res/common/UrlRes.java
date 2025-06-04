package com.github.dactiv.framework.fasc.res.common;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @author Fadada
 * 2021/10/16 17:47:36
 */
public class UrlRes extends BaseBean {
    private String eUrl;
    private String cloudUrl;

    public String geteUrl() {
        return eUrl;
    }

    public void seteUrl(String eUrl) {
        this.eUrl = eUrl;
    }

    public String getCloudUrl() {
        return cloudUrl;
    }

    public void setCloudUrl(String cloudUrl) {
        this.cloudUrl = cloudUrl;
    }
}
