package com.github.dactiv.framework.commons.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 系统异常基类
 *
 * @author maurice
 */
public class SystemException extends RuntimeException {

    public final static Logger LOGGER = LoggerFactory.getLogger(SystemException.class);

    @Serial
    private static final long serialVersionUID = 1464832006579973342L;

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

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new SystemException(message);
        }
    }

    public static void isTrue(boolean expression, Supplier<? extends SystemException> exception) {
        if (!expression) {
            throw exception.get();
        }
    }

    public static void convertRunnable(RunnableWithException runnable,
                                       Function<Exception, ? extends RuntimeException> function) {

        try {
            runnable.run();
        } catch (Exception e) {
            if(Objects.isNull(function)) {
                LOGGER.warn("convertRunnable 执行时出现错误，返回 null 值", e);
                return ;
            }
            throw function.apply(e);
        }
    }

    public static <T> T convertSupplier(SupplierWithException<T> supplier) {
        return convertSupplier(supplier, (String)null);
    }

    public static void convertRunnable(RunnableWithException runnable,
                                       String message) {

        try {
            runnable.run();
        } catch (Exception e) {
            if (Objects.isNull(message)) {
                LOGGER.warn("convertRunnable 执行时出现错误，返回 null 值", e);
                return ;
            }

            if (e instanceof RuntimeException runtimeException) {
                throw runtimeException;
            } else if (StringUtils.isEmpty(message)) {
                throw new SystemException(e);
            }

            throw new SystemException(message, e);
        }
    }

    public static <T> T convertSupplier(SupplierWithException<T> supplier,
                                        Function<Exception, ? extends RuntimeException> function) {

        try {
            return supplier.get();
        } catch (Exception e) {
            if (Objects.isNull(function)) {
                LOGGER.warn("convertSupplier 执行时出现错误，返回 null 值", e);
                return null;
            }

            RuntimeException runtimeException = function.apply(e);
            if (Objects.isNull(runtimeException)) {
                return null;
            }

            throw runtimeException;
        }
    }

    public static <T> T convertSupplier(SupplierWithException<T> supplier,
                                        String message) {
        try {
            return supplier.get();
        } catch (Exception e) {
            if (Objects.isNull(message)) {
                LOGGER.warn("convertSupplier 执行时出现错误，返回 null 值", e);
                return null;
            }

            if (e instanceof RuntimeException runtimeException) {
                throw runtimeException;
            } else if (StringUtils.isEmpty(message)) {
                throw new SystemException(e);
            } else {
                throw new SystemException(message, e);
            }
        }
    }

    // 自定义无返回值的函数式接口
    @FunctionalInterface
    public interface RunnableWithException {
        void run() throws Exception;
    }

    @FunctionalInterface
    public interface SupplierWithException<T> {
        T get() throws Exception;
    }
}

