package com.github.dactiv.framework.commons.exception;

import java.io.Serial;

/**
 * key value 枚举找不到内容异常
 *
 * @author maurice
 */
public class ValueEnumNotFoundException extends SystemException {

    @Serial
    private static final long serialVersionUID = 743566104062686552L;

    /**
     * key value 枚举找不到内容异常
     */
    public ValueEnumNotFoundException() {
        super();
    }

    /**
     * key value 枚举找不到内容异常
     *
     * @param message 异常信息
     */
    public ValueEnumNotFoundException(String message) {
        super(message);
    }

    /**
     * key value 枚举找不到内容异常
     *
     * @param message 异常信息
     * @param cause   异常类
     *
     * @since 1.4
     */
    public ValueEnumNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * key value 枚举找不到内容异常
     *
     * @param cause 异常类
     *
     * @since 1.4
     */
    public ValueEnumNotFoundException(Throwable cause) {
        super(cause);
    }
}

