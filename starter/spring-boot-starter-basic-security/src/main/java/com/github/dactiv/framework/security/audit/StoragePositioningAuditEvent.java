package com.github.dactiv.framework.security.audit;

import org.springframework.boot.actuate.audit.AuditEvent;

import java.time.Instant;
import java.util.Map;

/**
 * 根据 target 存储指定位置的审计事件
 *
 * @author maurice.chen
 */
public class StoragePositioningAuditEvent extends AuditEvent {

    /**
     * 存储定位
     */
    private String storagePositioning;

    public StoragePositioningAuditEvent(String storagePositioning, String principal, String type, Map<String, Object> data) {
        super(principal, type, data);
    }

    public StoragePositioningAuditEvent(String storagePositioning, String principal, String type, String... data) {
        super(principal, type, data);
    }

    public StoragePositioningAuditEvent(String storagePositioning, Instant timestamp, String principal, String type, Map<String, Object> data) {
        super(timestamp, principal, type, data);
        this.storagePositioning = storagePositioning;
    }

    /**
     * 获取存储定位
     */
    public String getStoragePositioning() {
        return storagePositioning;
    }
}
