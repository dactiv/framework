package com.github.dactiv.framework.fasc.res.doc;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @author zpt
 * @date 2023/6/28
 */
public class GetCompareResultDataRes extends BaseBean {
    private Object compareData;

    public Object getCompareData() {
        return compareData;
    }

    public void setCompareData(Object compareData) {
        this.compareData = compareData;
    }
}