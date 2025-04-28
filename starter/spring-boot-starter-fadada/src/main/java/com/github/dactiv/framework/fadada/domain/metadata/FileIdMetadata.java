package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

public class FileIdMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -8804297307963259963L;

    private String fileId;
    private String fileType;
    private String fddFileUrl;
    private String fileName;
    private String fileTotalPages;

    public FileIdMetadata() {
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFddFileUrl() {
        return this.fddFileUrl;
    }

    public void setFddFileUrl(String fddFileUrl) {
        this.fddFileUrl = fddFileUrl;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileTotalPages() {
        return this.fileTotalPages;
    }

    public void setFileTotalPages(String fileTotalPages) {
        this.fileTotalPages = fileTotalPages;
    }
}
