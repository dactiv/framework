package com.github.dactiv.framework.fasc.res.event;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;
import com.github.dactiv.framework.fasc.bean.common.OpenId;

/**
 * @author Fadada
 * 2021/10/21 15:49:48
 */
public class SignTaskFillRejectedInfo extends BaseBean {
    private String eventTime;
    private String signTaskId;
    private String signTaskStatus;
    private String actorId;
    private OpenId actorOpenId;
    private String fillRejectReason;
    private String signRejectReason;
    private String userName;

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

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public OpenId getActorOpenId() {
        return actorOpenId;
    }

    public void setActorOpenId(OpenId actorOpenId) {
        this.actorOpenId = actorOpenId;
    }

    public String getFillRejectReason() {
        return fillRejectReason;
    }

    public void setFillRejectReason(String fillRejectReason) {
        this.fillRejectReason = fillRejectReason;
    }

    public String getSignRejectReason() {
        return signRejectReason;
    }

    public void setSignRejectReason(String signRejectReason) {
        this.signRejectReason = signRejectReason;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
