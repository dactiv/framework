package com.github.dactiv.framework.fadada.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

/**
 * 存储类型
 *
 * @author maurice.chen
 */
public enum StorageType implements NameValueEnum<String> {

    /**
     * 上传至法大大云端
     */
    CLOUD("cloud", "上传至法大大云端"),
    /**
     * 上传至接入方本地服务器
     */
    OPDM("opdm","上传至接入方本地服务器")

    ;

    StorageType(String value, String name) {
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
