package com.github.dactiv.framework.minio;

import com.github.dactiv.framework.commons.minio.FileObject;
import com.github.dactiv.framework.commons.minio.FilenameObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserMetadataFileObject extends FileObject {

    private Map<String, String> userMetadata = new LinkedHashMap<>();

    public UserMetadataFileObject(FileObject fileObject, Map<String, String> userMetadata) {
        this.setObjectName(fileObject.getObjectName());
        this.setBucketName(fileObject.getBucketName());
        setUserMetadata(userMetadata);
    }

    public UserMetadataFileObject(FilenameObject fileObject) {
        this.setObjectName(fileObject.getObjectName());
        this.setBucketName(fileObject.getBucketName());

        Map<String, String> userMetadata = new LinkedHashMap<>();
        userMetadata.put(FilenameObject.MINIO_ORIGINAL_FILE_NAME, fileObject.getFilename());
        setUserMetadata(userMetadata);
    }

    public Map<String, String> getUserMetadata() {
        return userMetadata;
    }

    public void setUserMetadata(Map<String, String> userMetadata) {
        this.userMetadata = userMetadata;
    }
}
