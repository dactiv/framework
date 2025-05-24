package com.github.dactiv.framework.socketio.domain.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dactiv.framework.commons.RestResult;

import java.io.Serial;
import java.io.Serializable;

/**
 * socket 消息对象
 *
 * @author maurice
 */
public abstract class AbstractSocketMessageMetadata<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6021283894540422481L;
    /**
     * 事件类型
     */
    private String event;

    /**
     * rest 结果集
     */
    private RestResult<T> message;

    /**
     * 获取消息类型
     *
     * @return 消息类型
     */
    @JsonIgnore
    public abstract String getType();

    public AbstractSocketMessageMetadata() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public RestResult<T> getMessage() {
        return message;
    }

    public void setMessage(RestResult<T> message) {
        this.message = message;
    }
}
