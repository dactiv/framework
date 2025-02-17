package com.github.dactiv.framework.mybatis.config;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.security.StoragePositionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 操作日志追踪配置
 *
 * @author maurice.chen
 */
@ConfigurationProperties("dactiv.mybatis.operation-data-trace")
public class OperationDataTraceProperties {

    public static final String DEFAULT_AUDIT_PREFIX_NAME = "OPERATION_DATA_AUDIT";

    /**
     * 审计前缀名称
     */
    private String auditPrefixName = DEFAULT_AUDIT_PREFIX_NAME;

    /**
     * 日志格式化内容
     */
    private String dateFormat = Casts.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

    /**
     * 存储定位解析器
     */
    private StoragePositionProperties storagePosition;

    public OperationDataTraceProperties() {
    }

    public String getAuditPrefixName() {
        return auditPrefixName;
    }

    public void setAuditPrefixName(String auditPrefixName) {
        this.auditPrefixName = auditPrefixName;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public StoragePositionProperties getStoragePosition() {
        return storagePosition;
    }

    public void setStoragePosition(StoragePositionProperties storagePosition) {
        this.storagePosition = storagePosition;
    }
}
