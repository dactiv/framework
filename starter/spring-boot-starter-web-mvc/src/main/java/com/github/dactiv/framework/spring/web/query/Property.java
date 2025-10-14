package com.github.dactiv.framework.spring.web.query;

import com.github.dactiv.framework.commons.Casts;
import org.apache.commons.lang3.StringUtils;

/**
 * 属性信息，用户记录对应字段名称和值信息
 *
 * @author maurice.chen
 */
public class Property {

    /**
     * 值字段
     */
    public static final String VALUE_FIELD = "value";

    /**
     * 属性名称字段
     */
    public static final String PROPERTY_NAME = "propertyName";

    /**
     * 别名
     */
    private String alias = StringUtils.EMPTY;

    /**
     * 属性名
     */
    private String propertyName;

    /**
     * 值
     */
    private Object value;

    public Property(String propertyName) {
        this.propertyName = propertyName;
    }

    public Property(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public Property(String alias, String propertyName, Object value) {
        this.alias = alias;
        this.propertyName = propertyName;
        this.value = value;
    }

    /**
     * 获取属性名称
     *
     * @return 属性名称
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 设置属性名称
     *
     * @param propertyName 属性名称
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getFinalPropertyName() {
        if (StringUtils.isNotEmpty(alias)) {
            return alias + Casts.DOT + propertyName;
        }
        return propertyName;
    }

    /**
     * 获取条件值
     *
     * @return 条件值
     */
    public Object getValue() {
        return value;
    }

    /**
     * 设置条件值
     *
     * @param value 条件值
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 获取别名
     * @return 别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置别名
     *
     * @param alias 别名
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String splicePropertyName(String field) {
        if (StringUtils.isNotEmpty(alias)) {
            return alias + Casts.DOT + field;
        }
        return field;
    }
}
