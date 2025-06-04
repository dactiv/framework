package com.github.dactiv.framework.fadada.domain.metadata.doc;

import java.io.Serial;
import java.io.Serializable;

public class FddFileUrlMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = 7882336491687175515L;

    private String fileType;
    private String fddFileUrl;
    private String fileName;
    private String fileFormat;

    public FddFileUrlMetadata() {
    }

    public FddFileUrlMetadata(String fileType, String fddFileUrl, String fileName) {
        this.fileType = fileType;
        this.fddFileUrl = fddFileUrl;
        this.fileName = fileName;
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

    public String getFileFormat() {
        return this.fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
}
