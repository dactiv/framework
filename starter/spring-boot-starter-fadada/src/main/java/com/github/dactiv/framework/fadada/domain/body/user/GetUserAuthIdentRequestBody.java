package com.github.dactiv.framework.fadada.domain.body.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class GetUserAuthIdentRequestBody implements Serializable {

    @Serial
    private static final long serialVersionUID = -7115547443515887318L;

    private String userName;
    private String userIdentType;
    private String userIdentNo;
    private String mobile;
    private String bankAccountNo;
    private List<String> identMethod;
    @JsonProperty("faceauthMode")
    private String faceAuthMode;

    public GetUserAuthIdentRequestBody() {
    }

    public String getFaceAuthMode() {
        return this.faceAuthMode;
    }

    public void setFaceAuthMode(String faceAuthMode) {
        this.faceAuthMode = faceAuthMode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdentType() {
        return this.userIdentType;
    }

    public void setUserIdentType(String userIdentType) {
        this.userIdentType = userIdentType;
    }

    public String getUserIdentNo() {
        return this.userIdentNo;
    }

    public void setUserIdentNo(String userIdentNo) {
        this.userIdentNo = userIdentNo;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankAccountNo() {
        return this.bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public List<String> getIdentMethod() {
        return this.identMethod;
    }

    public void setIdentMethod(List<String> identMethod) {
        this.identMethod = identMethod;
    }
}
