package com.github.dactiv.framework.fadada.domain.body;

import java.io.Serial;
import java.io.Serializable;

public class PageableRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 1627972225092054312L;

    private Integer listPageNo;
    private Integer listPageSize;

    public PageableRequestBody() {

    }

    public Integer getListPageNo() {
        return listPageNo;
    }

    public void setListPageNo(Integer listPageNo) {
        this.listPageNo = listPageNo;
    }

    public Integer getListPageSize() {
        return listPageSize;
    }

    public void setListPageSize(Integer listPageSize) {
        this.listPageSize = listPageSize;
    }
}
