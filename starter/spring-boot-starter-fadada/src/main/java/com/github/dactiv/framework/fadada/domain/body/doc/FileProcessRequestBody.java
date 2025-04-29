package com.github.dactiv.framework.fadada.domain.body.doc;

import com.github.dactiv.framework.fadada.domain.metadata.doc.FddFileUrlMetadata;
import com.github.dactiv.framework.fadada.enumerate.StorageType;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class FileProcessRequestBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 2756013895566833060L;
    private List<FddFileUrlMetadata> fddFileUrlList;
    private String storageType = StorageType.CLOUD.getValue();
    private Boolean separation = false;

    public FileProcessRequestBody() {
    }

    public FileProcessRequestBody(List<FddFileUrlMetadata> fddFileUrlList) {
        this(fddFileUrlList, StorageType.CLOUD.getValue(), false);
    }

    public FileProcessRequestBody(List<FddFileUrlMetadata> fddFileUrlList, String storageType) {
        this(fddFileUrlList, storageType, false);
    }

    public FileProcessRequestBody(List<FddFileUrlMetadata> fddFileUrlList, String storageType, Boolean separation) {
        this.fddFileUrlList = fddFileUrlList;
        this.storageType = storageType;
        this.separation = separation;
    }

    public String getStorageType() {
        return this.storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public Boolean getSeparation() {
        return this.separation;
    }

    public void setSeparation(Boolean separation) {
        this.separation = separation;
    }

    public List<FddFileUrlMetadata> getFddFileUrlList() {
        return this.fddFileUrlList;
    }

    public void setFddFileUrlList(List<FddFileUrlMetadata> fddFileUrlList) {
        this.fddFileUrlList = fddFileUrlList;
    }
}
