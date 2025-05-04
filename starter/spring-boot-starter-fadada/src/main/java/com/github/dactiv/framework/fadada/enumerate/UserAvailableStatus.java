package com.github.dactiv.framework.fadada.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

public enum UserAvailableStatus implements NameValueEnum<String> {

    /**
     * 禁用状态
     */
    DISABLE("disable", "禁用状态"),
    /**
     * 启用状态
     */
    ENABLE("enable","启用状态")

    ;

    UserAvailableStatus(String value, String name) {
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
