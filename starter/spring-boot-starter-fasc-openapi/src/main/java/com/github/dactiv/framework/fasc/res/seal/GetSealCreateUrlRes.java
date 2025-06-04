package com.github.dactiv.framework.fasc.res.seal;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @Author： Fadada
 * @Date: 2022/10/8
 */
public class GetSealCreateUrlRes extends BaseBean {

    /**
     * 创建印章的页面链接
     */
    private String sealCreateUrl;

    public String getSealCreateUrl() {
        return sealCreateUrl;
    }

    public void setSealCreateUrl(String sealCreateUrl) {
        this.sealCreateUrl = sealCreateUrl;
    }
}
