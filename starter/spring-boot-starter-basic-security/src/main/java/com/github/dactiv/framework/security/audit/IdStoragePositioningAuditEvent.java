package com.github.dactiv.framework.security.audit;

import com.github.dactiv.framework.commons.id.BasicIdentification;

import java.time.Instant;
import java.util.Map;

/**
 * 带 id 的 存储指定位置的审计事件
 *
 * @author maurice.chen
 */
public class IdStoragePositioningAuditEvent extends StoragePositioningAuditEvent implements BasicIdentification<String> {

    /**
     * 主键 id
     */
    private String id;

    public IdStoragePositioningAuditEvent(String id, StoragePositioningAuditEvent auditEvent) {
        this(
                id,
                auditEvent.getStoragePositioning(),
                auditEvent.getTimestamp(),
                auditEvent.getPrincipal(),
                auditEvent.getType(),
                auditEvent.getData()
        );
    }

    public IdStoragePositioningAuditEvent(String id,
                                          String storagePositioning,
                                          String principal,
                                          String type,
                                          Map<String, Object> data) {
        this(id, storagePositioning, Instant.now(), principal, type, data);
    }

    public IdStoragePositioningAuditEvent(String id,
                                          String storagePositioning,
                                          Instant timestamp,
                                          String principal,
                                          String type,
                                          Map<String, Object> data) {
        super(storagePositioning, timestamp, principal, type, data);
        this.id = id;
    }

    /**
     * 获取主键 id
     *
     * @return 主键 id
     */
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
