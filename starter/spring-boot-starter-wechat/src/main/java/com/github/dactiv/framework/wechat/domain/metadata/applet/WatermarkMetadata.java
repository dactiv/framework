package com.github.dactiv.framework.wechat.domain.metadata.applet;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class WatermarkMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -3010541299304006901L;
    /**
     * 用户获取手机号操作的时间戳
     */
    private Date timestamp;

    /**
     * 小程序 appid
     */
    private String appId;

    public WatermarkMetadata() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
