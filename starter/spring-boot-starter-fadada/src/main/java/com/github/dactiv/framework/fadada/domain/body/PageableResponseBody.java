package com.github.dactiv.framework.fadada.domain.body;

import java.io.Serial;

public class PageableResponseBody extends PageableRequestBody {

    @Serial
    private static final long serialVersionUID = -2510570416253496502L;

    private Integer totalCount;

    public PageableResponseBody() {
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
