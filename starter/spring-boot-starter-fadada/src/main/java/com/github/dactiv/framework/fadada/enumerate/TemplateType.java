package com.github.dactiv.framework.fadada.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

/**
 * 模版类型
 *
 * @author maurice.chen
 */
public enum TemplateType implements NameValueEnum<String> {

    /**
     * 上传至法大大云端
     */
    DOC("doc", "文档模板"),
    /**
     * 上传至接入方本地服务器
     */
    SIGN("sign","签署模板")

    ;

    TemplateType(String value, String name) {
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
