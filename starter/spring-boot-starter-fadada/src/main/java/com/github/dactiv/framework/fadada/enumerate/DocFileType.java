package com.github.dactiv.framework.fadada.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

/**
 * 文件类型
 *
 * @author maurice.chen
 */
public enum DocFileType implements NameValueEnum<String> {

    /**
     * 签署（签字盖章）的文档
     */
    DOC("doc", "签署（签字盖章）的文档"),
    /**
     * 签署的附件查看
     */
    ATTACH("attach","签署的附件查看")

    ;

    DocFileType(String value, String name) {
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
