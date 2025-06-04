package com.github.dactiv.framework.fasc.req.doc;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

public class FileVerifySignReq extends BaseReq {

    private String fileId;
    private String fileHash;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
}
