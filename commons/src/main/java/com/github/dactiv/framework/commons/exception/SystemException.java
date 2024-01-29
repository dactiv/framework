package com.github.dactiv.framework.commons.exception;

import java.io.Serial;

/**
 * 系统异常基类
 *
 * @author maurice
 */
public class SystemException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2623553019157158620L;

    /**
     * 系统异常
     */
    public SystemException() {
        super();
    }

    /**
     * 系统异常
     *
     * @param message 异常信息
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * 系统异常
     *
     * @param message 异常信息
     * @param cause   异常类
     *
     * @since 1.4
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 系统异常
     *
     * @param cause 异常类
     *
     * @since 1.4
     */
    public SystemException(Throwable cause) {
        super(cause);
    }

}

