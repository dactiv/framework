package com.github.dactiv.framework.fadada.domain.body.doc;

import com.github.dactiv.framework.fadada.domain.metadata.FileIdMetadata;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class FileProcessResponseBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -8785655339977948821L;
    private List<FileIdMetadata> fileIdList;

    public FileProcessResponseBody() {
    }

    public List<FileIdMetadata> getFileIdList() {
        return this.fileIdList;
    }

    public void setFileIdList(List<FileIdMetadata> fileIdList) {
        this.fileIdList = fileIdList;
    }
}
