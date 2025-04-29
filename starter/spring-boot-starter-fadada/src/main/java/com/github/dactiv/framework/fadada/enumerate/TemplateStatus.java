package com.github.dactiv.framework.fadada.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

/**
 * 模版状态
 *
 * @author maurice.chen
 */
public enum TemplateStatus implements NameValueEnum<String> {

    /**
     * 上传至法大大云端
     */
    INVALID("invalid", "停用"),
    /**
     * 上传至接入方本地服务器
     */
    VALID("valid","启用")

    ;

    TemplateStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    private final String value;
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
