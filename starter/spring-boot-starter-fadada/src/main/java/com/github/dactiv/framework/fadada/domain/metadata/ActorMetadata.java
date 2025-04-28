package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ActorMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -9090735014242536148L;

    private String actorId;
    private String actorType;
    private String actorName;
    private List<String> permissions;
    private String actorOpenId;
    private String actorFddId;
    private String actorEntityId;
    private List<ActorCorpMemberMetadata> actorCorpMembers;
    private String identNameForMatch;
    private String certType;
    private String certNoForMatch;
    private String accountName;
    private Boolean accountEditable;
    private NotificationMetadata notification;
    private Boolean sendNotification;
    private List<String> notifyType;
    private String notifyAddress;
    private String clientUserId;
    private List<String> authScopes = new LinkedList<>();

    public ActorMetadata() {
    }

    public Boolean getAccountEditable() {
        return this.accountEditable;
    }

    public void setAccountEditable(Boolean accountEditable) {
        this.accountEditable = accountEditable;
    }

    public String getClientUserId() {
        return this.clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public List<String> getAuthScopes() {
        return this.authScopes;
    }

    public void setAuthScopes(List<String> authScopes) {
        this.authScopes = authScopes;
    }

    public String getActorEntityId() {
        return this.actorEntityId;
    }

    public void setActorEntityId(String actorEntityId) {
        this.actorEntityId = actorEntityId;
    }

    public String getActorType() {
        return this.actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public String getActorId() {
        return this.actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return this.actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getActorOpenId() {
        return this.actorOpenId;
    }

    public void setActorOpenId(String actorOpenId) {
        this.actorOpenId = actorOpenId;
    }

    public String getActorFddId() {
        return this.actorFddId;
    }

    public void setActorFddId(String actorFddId) {
        this.actorFddId = actorFddId;
    }

    public List<ActorCorpMemberMetadata> getActorCorpMembers() {
        return this.actorCorpMembers;
    }

    public void setActorCorpMembers(List<ActorCorpMemberMetadata> actorCorpMembers) {
        this.actorCorpMembers = actorCorpMembers;
    }

    public String getIdentNameForMatch() {
        return this.identNameForMatch;
    }

    public void setIdentNameForMatch(String identNameForMatch) {
        this.identNameForMatch = identNameForMatch;
    }

    public String getCertNoForMatch() {
        return this.certNoForMatch;
    }

    public void setCertNoForMatch(String certNoForMatch) {
        this.certNoForMatch = certNoForMatch;
    }

    public NotificationMetadata getNotification() {
        return this.notification;
    }

    public void setNotification(NotificationMetadata notification) {
        this.notification = notification;
    }

    public String getCertType() {
        return this.certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Boolean getSendNotification() {
        return this.sendNotification;
    }

    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public List<String> getNotifyType() {
        return this.notifyType;
    }

    public void setNotifyType(List<String> notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyAddress() {
        return this.notifyAddress;
    }

    public void setNotifyAddress(String notifyAddress) {
        this.notifyAddress = notifyAddress;
    }
}
