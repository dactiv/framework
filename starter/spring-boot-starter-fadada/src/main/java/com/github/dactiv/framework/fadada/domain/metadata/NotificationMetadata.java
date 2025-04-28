package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

public class NotificationMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -5100392264702682912L;

    private Boolean sendNotification;
    private String notifyWay;
    private String notifyAddress;
    private Boolean appointAccount;
    private CustomNotifyMetadata customNotifyContent;

    public Boolean getSendNotification() {
        return this.sendNotification;
    }

    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public String getNotifyWay() {
        return this.notifyWay;
    }

    public void setNotifyWay(String notifyWay) {
        this.notifyWay = notifyWay;
    }

    public String getNotifyAddress() {
        return this.notifyAddress;
    }

    public void setNotifyAddress(String notifyAddress) {
        this.notifyAddress = notifyAddress;
    }

    public CustomNotifyMetadata getCustomNotifyContent() {
        return this.customNotifyContent;
    }

    public void setCustomNotifyContent(CustomNotifyMetadata customNotifyContent) {
        this.customNotifyContent = customNotifyContent;
    }

    public static NotificationMetadata getInstance(boolean sendNotification, String notifyWay, String notifyAddress) {
        return new NotificationMetadata(sendNotification, notifyWay, notifyAddress);
    }

    public NotificationMetadata(Boolean sendNotification, String notifyWay, String notifyAddress) {
        this.sendNotification = sendNotification;
        this.notifyWay = notifyWay;
        this.notifyAddress = notifyAddress;
    }

    public NotificationMetadata() {
    }

    public Boolean getAppointAccount() {
        return this.appointAccount;
    }

    public void setAppointAccount(Boolean appointAccount) {
        this.appointAccount = appointAccount;
    }
}
