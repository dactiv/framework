package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;

public class ActorNotificationMetadata extends NotificationMetadata {

    @Serial
    private static final long serialVersionUID = -5100392264702682912L;

    private Boolean sendNotification;
    private Boolean appointAccount;
    private CustomNotifyMetadata customNotifyContent;

    public Boolean getSendNotification() {
        return this.sendNotification;
    }

    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public CustomNotifyMetadata getCustomNotifyContent() {
        return this.customNotifyContent;
    }

    public void setCustomNotifyContent(CustomNotifyMetadata customNotifyContent) {
        this.customNotifyContent = customNotifyContent;
    }

    public static ActorNotificationMetadata getInstance(boolean sendNotification, String notifyWay, String notifyAddress) {
        return new ActorNotificationMetadata(sendNotification, notifyWay, notifyAddress);
    }

    public ActorNotificationMetadata(Boolean sendNotification, String notifyWay, String notifyAddress) {
        this.sendNotification = sendNotification;
        setNotifyWay(notifyWay);;
        setNotifyAddress(notifyAddress);
    }

    public ActorNotificationMetadata() {
    }

    public Boolean getAppointAccount() {
        return this.appointAccount;
    }

    public void setAppointAccount(Boolean appointAccount) {
        this.appointAccount = appointAccount;
    }
}
