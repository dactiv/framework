package com.github.dactiv.framework.socketio.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户通知
 *
 * @author maurice.chen
 */
public class UserNoticeMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -5755355478347504037L;

    public static final String SYSTEM_USER_NOTICE_EVENT_NAME = "SYSTEM_USER_NOTICE_EVENT";

    /**
     * 标题
     */
    private String title;

    /**
     * 消息
     */
    private String message;

    /**
     * 类型
     */
    private String type;

    public UserNoticeMetadata() {
    }

    public UserNoticeMetadata(String title, String message, String type) {
        this.title = title;
        this.message = message;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static UserNoticeMetadata of(String title, String message, String type) {
        return new UserNoticeMetadata(title, message, type);
    }
}
