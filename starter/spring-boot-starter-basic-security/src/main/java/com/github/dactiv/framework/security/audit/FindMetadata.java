package com.github.dactiv.framework.security.audit;

import java.util.Map;

public class FindMetadata<T> {

    private T targetQuery;

    private String storagePositioning;

    private Map<String, Object> query;

    public FindMetadata() {
    }

    public FindMetadata(T targetQuery, String storagePositioning, Map<String, Object> query) {
        this.targetQuery = targetQuery;
        this.storagePositioning = storagePositioning;
        this.query = query;
    }

    public T getTargetQuery() {
        return targetQuery;
    }

    public void setTargetQuery(T targetQuery) {
        this.targetQuery = targetQuery;
    }

    public String getStoragePositioning() {
        return storagePositioning;
    }

    public void setStoragePositioning(String storagePositioning) {
        this.storagePositioning = storagePositioning;
    }

    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }
}
