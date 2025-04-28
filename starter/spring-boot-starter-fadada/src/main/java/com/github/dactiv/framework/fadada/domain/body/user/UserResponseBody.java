package com.github.dactiv.framework.fadada.domain.body.user;

import com.github.dactiv.framework.fadada.domain.metadata.user.UserIdentMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.user.UserInfoMetadata;

import java.io.Serial;
import java.io.Serializable;

public class UserResponseBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 8630087348213288465L;

    private String identStatus;
    private UserIdentMetadata userIdentInfo;
    private UserInfoMetadata userIdentInfoExtend;
    private String identMethod;
    private String identSubmitTime;
    private String identSuccessTime;
    private String openUserId;
    private String fddId;
    private String facePicture;
    private String accountName;
    private String verifyInfoUrl;

    public String getIdentStatus() {
        return identStatus;
    }

    public void setIdentStatus(String identStatus) {
        this.identStatus = identStatus;
    }

    public UserIdentMetadata getUserIdentInfo() {
        return userIdentInfo;
    }

    public void setUserIdentInfo(UserIdentMetadata userIdentInfo) {
        this.userIdentInfo = userIdentInfo;
    }

    public UserInfoMetadata getUserIdentInfoExtend() {
        return userIdentInfoExtend;
    }

    public void setUserIdentInfoExtend(UserInfoMetadata userIdentInfoExtend) {
        this.userIdentInfoExtend = userIdentInfoExtend;
    }

    public String getIdentMethod() {
        return identMethod;
    }

    public void setIdentMethod(String identMethod) {
        this.identMethod = identMethod;
    }

    public String getIdentSubmitTime() {
        return identSubmitTime;
    }

    public void setIdentSubmitTime(String identSubmitTime) {
        this.identSubmitTime = identSubmitTime;
    }

    public String getIdentSuccessTime() {
        return identSuccessTime;
    }

    public void setIdentSuccessTime(String identSuccessTime) {
        this.identSuccessTime = identSuccessTime;
    }

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }

    public String getFddId() {
        return fddId;
    }

    public void setFddId(String fddId) {
        this.fddId = fddId;
    }

    public String getFacePicture() {
        return facePicture;
    }

    public void setFacePicture(String facePicture) {
        this.facePicture = facePicture;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getVerifyInfoUrl() {
        return verifyInfoUrl;
    }

    public void setVerifyInfoUrl(String verifyInfoUrl) {
        this.verifyInfoUrl = verifyInfoUrl;
    }
}
