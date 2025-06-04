package com.github.dactiv.framework.fasc.res.event;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @author Fadada
 * 2021/10/21 15:49:48
 */
public class SignTaskCanceledInfo extends BaseBean {
    private String eventTime;
    private String signTaskId;
    private String signTaskStatus;

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }

    public String getSignTaskStatus() {
        return signTaskStatus;
    }

    public void setSignTaskStatus(String signTaskStatus) {
        this.signTaskStatus = signTaskStatus;
    }
}
