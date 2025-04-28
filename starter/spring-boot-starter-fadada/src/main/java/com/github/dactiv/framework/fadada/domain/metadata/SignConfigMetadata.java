package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SignConfigMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = 8279360117338095103L;

    private Integer orderNo;
    private Boolean blockHere;
    private Boolean requestVerifyFree;
    private Boolean requestMemberSign;
    private List<String> verifyMethods;
    private Boolean joinByLink;
    private String signerSignMethod;
    private Boolean readingToEnd;
    private String readingTime;
    private Boolean identifiedView;
    private Boolean resizeSeal;
    private List<String> audioVideoInfo;
    private List<AudioVideoMetadata> audioVideoInfos;
    private List<ActorAttachMetadata> actorAttachInfos;
    private Boolean freeLogin;
    private Boolean viewCompletedTask;
    private Long freeDragSealId;
    private Boolean signAllDoc;
    private Boolean authorizeFreeSign;
    private String multiFactor;

    public SignConfigMetadata() {
    }

    public String getMultiFactor() {
        return this.multiFactor;
    }

    public void setMultiFactor(String multiFactor) {
        this.multiFactor = multiFactor;
    }

    public List<AudioVideoMetadata> getAudioVideoInfos() {
        return this.audioVideoInfos;
    }

    public void setAudioVideoInfos(List<AudioVideoMetadata> audioVideoInfos) {
        this.audioVideoInfos = audioVideoInfos;
    }

    public Boolean getAuthorizeFreeSign() {
        return this.authorizeFreeSign;
    }

    public void setAuthorizeFreeSign(Boolean authorizeFreeSign) {
        this.authorizeFreeSign = authorizeFreeSign;
    }

    public Boolean getRequestMemberSign() {
        return this.requestMemberSign;
    }

    public void setRequestMemberSign(Boolean requestMemberSign) {
        this.requestMemberSign = requestMemberSign;
    }

    public Integer getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getSignerSignMethod() {
        return this.signerSignMethod;
    }

    public void setSignerSignMethod(String signerSignMethod) {
        this.signerSignMethod = signerSignMethod;
    }

    public Boolean getReadingToEnd() {
        return this.readingToEnd;
    }

    public void setReadingToEnd(Boolean readingToEnd) {
        this.readingToEnd = readingToEnd;
    }

    public String getReadingTime() {
        return this.readingTime;
    }

    public void setReadingTime(String readingTime) {
        this.readingTime = readingTime;
    }

    public Boolean getBlockHere() {
        return this.blockHere;
    }

    public void setBlockHere(Boolean blockHere) {
        this.blockHere = blockHere;
    }

    public Boolean getRequestVerifyFree() {
        return this.requestVerifyFree;
    }

    public void setRequestVerifyFree(Boolean requestVerifyFree) {
        this.requestVerifyFree = requestVerifyFree;
    }

    public List<String> getVerifyMethods() {
        return this.verifyMethods;
    }

    public void setVerifyMethods(List<String> verifyMethods) {
        this.verifyMethods = verifyMethods;
    }

    public Boolean getJoinByLink() {
        return this.joinByLink;
    }

    public void setJoinByLink(Boolean joinByLink) {
        this.joinByLink = joinByLink;
    }

    public Boolean getIdentifiedView() {
        return this.identifiedView;
    }

    public void setIdentifiedView(Boolean identifiedView) {
        this.identifiedView = identifiedView;
    }

    public Boolean getResizeSeal() {
        return this.resizeSeal;
    }

    public void setResizeSeal(Boolean resizeSeal) {
        this.resizeSeal = resizeSeal;
    }

    public List<String> getAudioVideoInfo() {
        return this.audioVideoInfo;
    }

    public void setAudioVideoInfo(List<String> audioVideoInfo) {
        this.audioVideoInfo = audioVideoInfo;
    }

    public List<ActorAttachMetadata> getActorAttachInfos() {
        return this.actorAttachInfos;
    }

    public void setActorAttachInfos(List<ActorAttachMetadata> actorAttachInfos) {
        this.actorAttachInfos = actorAttachInfos;
    }

    public Boolean getFreeLogin() {
        return this.freeLogin;
    }

    public void setFreeLogin(Boolean freeLogin) {
        this.freeLogin = freeLogin;
    }

    public Boolean getViewCompletedTask() {
        return this.viewCompletedTask;
    }

    public void setViewCompletedTask(Boolean viewCompletedTask) {
        this.viewCompletedTask = viewCompletedTask;
    }

    public Long getFreeDragSealId() {
        return this.freeDragSealId;
    }

    public void setFreeDragSealId(Long freeDragSealId) {
        this.freeDragSealId = freeDragSealId;
    }

    public Boolean getSignAllDoc() {
        return this.signAllDoc;
    }

    public void setSignAllDoc(Boolean signAllDoc) {
        this.signAllDoc = signAllDoc;
    }
}
