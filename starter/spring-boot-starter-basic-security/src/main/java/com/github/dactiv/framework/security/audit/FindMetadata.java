package com.github.dactiv.framework.security.audit;

import java.time.Instant;
import java.util.Map;

public class FindMetadata<T> {

    private T targetQuery;

    private String storagePositioning;

    private Map<String, Object> query;

    private Instant after;

    public FindMetadata() {
    }

    public FindMetadata(T targetQuery, String storagePositioning, Map<String, Object> query, Instant after) {
        this.targetQuery = targetQuery;
        this.storagePositioning = storagePositioning;
        this.query = query;
        this.after = after;
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

    public Instant getAfter() {
        return after;
    }

    public void setAfter(Instant after) {
        this.after = after;
    }
}
