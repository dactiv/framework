package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;

public class WechatAppletMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 6555697068013624057L;

    private String wxOriginalId;
    private String path;

    public WechatAppletMetadata() {

    }

    public String getWxOriginalId() {
        return wxOriginalId;
    }

    public void setWxOriginalId(String wxOriginalId) {
        this.wxOriginalId = wxOriginalId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
