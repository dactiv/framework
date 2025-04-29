package com.github.dactiv.framework.fadada.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

/**
 * open id 类型
 *
 * @author maurice.chen
 */
public enum OpenIdType implements NameValueEnum<String> {

    /**
     * 个人
     */
    PERSON("person", "个人"),
    /**
     * 企业
     */
    CORP("corp","企业")

    ;

    OpenIdType(String value, String name) {
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
