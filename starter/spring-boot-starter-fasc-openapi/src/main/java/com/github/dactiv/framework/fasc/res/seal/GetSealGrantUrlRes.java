package com.github.dactiv.framework.fasc.res.seal;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @Author： Fadada
 * @Date: 2022/10/8
 */
public class GetSealGrantUrlRes extends BaseBean {

    /**
     * 印章授权的页面链接
     */
    private String sealGrantUrl;

    public String getSealGrantUrl() {
        return sealGrantUrl;
    }

    public void setSealGrantUrl(String sealGrantUrl) {
        this.sealGrantUrl = sealGrantUrl;
    }
}
