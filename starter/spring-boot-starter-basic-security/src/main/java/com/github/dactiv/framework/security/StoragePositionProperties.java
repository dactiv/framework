package com.github.dactiv.framework.security;

import com.github.dactiv.framework.commons.Casts;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 审计索引配置
 *
 * @author maurice.chen
 */
@ConfigurationProperties("dactiv.security.audit.storage-position")
public class StoragePositionProperties implements Serializable {

    @Deprecated
    public static final String DEFAULT_PATTERN = "yyyy_MM_dd";
    /**
     * 前缀
     */
    private String prefix = "audit_event";

    /**
     * 分隔符
     */
    private String separator = Casts.UNDERSCORE;

    /**
     * spring el 表达式
     */
    private List<String> springExpression = new LinkedList<>();

    public StoragePositionProperties() {
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public List<String> getSpringExpression() {
        return springExpression;
    }

    public void setSpringExpression(List<String> springExpression) {
        this.springExpression = springExpression;
    }
}
