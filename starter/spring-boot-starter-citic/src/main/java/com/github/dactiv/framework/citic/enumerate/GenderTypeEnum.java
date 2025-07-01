package com.github.dactiv.framework.citic.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;


public enum GenderTypeEnum implements NameValueEnum<String> {

    FEMALE("0", "女"),
    MALE("1", "男");

    ;

    GenderTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    private final String value;

    private final String name;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
