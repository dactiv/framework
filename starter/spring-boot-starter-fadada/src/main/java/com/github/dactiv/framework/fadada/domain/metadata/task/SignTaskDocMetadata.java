package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;

public class SignTaskDocMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = 4574767549207506788L;

    private String docId;
    private String docName;
    private String docFileId;

    public SignTaskDocMetadata() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocFileId() {
        return docFileId;
    }

    public void setDocFileId(String docFileId) {
        this.docFileId = docFileId;
    }
}
