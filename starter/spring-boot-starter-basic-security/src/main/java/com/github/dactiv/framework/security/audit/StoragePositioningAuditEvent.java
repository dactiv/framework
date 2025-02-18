package com.github.dactiv.framework.security.audit;

import org.springframework.boot.actuate.audit.AuditEvent;

import java.time.Instant;
import java.util.Map;

/**
 * 存储指定位置的审计事件，用于根据 storagePositioning 修改默认存储位置的审计事件
 * 
 * @see com.github.dactiv.framework.security.audit.elasticsearch.ElasticsearchAuditEventRepository#doAdd(AuditEvent) 
 * @see com.github.dactiv.framework.security.audit.mongo.MongoAuditEventRepository#doAdd(AuditEvent)
 *
 * @author maurice.chen
 */
public class StoragePositioningAuditEvent extends AuditEvent {

    /**
     * 存储定位
     */
    private final String storagePositioning;

    public StoragePositioningAuditEvent(String storagePositioning, AuditEvent auditEvent) {
        this(
                storagePositioning,
                auditEvent.getTimestamp(),
                auditEvent.getPrincipal(),
                auditEvent.getType(),
                auditEvent.getData()
        );
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
