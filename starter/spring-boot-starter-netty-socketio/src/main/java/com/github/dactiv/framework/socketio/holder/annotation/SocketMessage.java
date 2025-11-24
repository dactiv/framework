package com.github.dactiv.framework.socketio.holder.annotation;


import java.lang.annotation.*;

/**
 * socket 消息注解，用于配合 {@link com.github.dactiv.framework.socketio.holder.SocketResultHolder} 使用，
 * 当方法使用该注解时，会根据 {@link com.github.dactiv.framework.socketio.holder.SocketResultHolder#get()} 的值自动发送 socket 消息
 *
 * @author maurice.chen
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SocketMessage {

    /**
     * 属性过滤 id
     *
     * @return id
     */
    String value() default "";

    /**
     * 是否忽略其他 id 值
     *
     * @return true 是，否则 false
     */
    boolean ignoreOtherIds() default false;
}
