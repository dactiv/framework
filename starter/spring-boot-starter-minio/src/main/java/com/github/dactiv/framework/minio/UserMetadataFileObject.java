package com.github.dactiv.framework.minio;

import com.github.dactiv.framework.commons.minio.FileObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserMetadataFileObject extends FileObject {

    private Map<String, String> userMetadata = new LinkedHashMap<>();

    public Map<String, String> getUserMetadata() {
        return userMetadata;
    }

    public void setUserMetadata(Map<String, String> userMetadata) {
        this.userMetadata = userMetadata;
    }
}
