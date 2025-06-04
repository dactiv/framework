package com.github.dactiv.framework.fasc.req.doc;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

import java.util.List;

public class GetKeywordPositionReq extends BaseReq {
    private String fileId;
    private List<String> keywords;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
