package com.github.dactiv.framework.socketio.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

/**
 * 连接状态
 *
 * @author maurice
 */
public enum ConnectStatus implements NameValueEnum<Integer> {

    /**
     * 断开链接
     */
    Disconnected("断开", 10),
    /**
     * 链接中
     */
    Connecting("连接中", 20),
    /**
     * 已链接
     */
    Connected("已链接", 30);

    ConnectStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private final String name;

    private final Integer value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
