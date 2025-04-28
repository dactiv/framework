package com.github.dactiv.framework.fadada.domain.body.doc;

import java.io.Serial;
import java.io.Serializable;

public class GetUploadUrlRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -4463545593560128746L;
    private String fileType = "doc";
    private String storageType = "cloud";

    public GetUploadUrlRequestBody() {
    }

    public GetUploadUrlRequestBody(String fileType) {
        this(fileType, "cloud");
    }

    public GetUploadUrlRequestBody(String fileType, String storageType) {
        this.fileType = fileType;
        this.storageType = storageType;
    }

    public String getStorageType() {
        return this.storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
