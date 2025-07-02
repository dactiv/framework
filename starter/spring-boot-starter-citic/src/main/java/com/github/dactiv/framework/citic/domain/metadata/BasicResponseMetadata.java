package com.github.dactiv.framework.citic.domain.metadata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serial;
import java.io.Serializable;

public class BasicResponseMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 7720848186082816691L;

    @JacksonXmlProperty(localName = "RSP_CODE")
    private String code;//应答码
    @JacksonXmlProperty(localName = "RSP_MSG")
    private String message;//应答码描述
    @JacksonXmlProperty(localName = "REQ_SSN")
    private String requestSsn;//发起方流水号
    @JacksonXmlProperty(localName = "PWDID")
    private String passwordId;//动态密码句柄
    @JacksonXmlProperty(localName = "TRANS_ID")
    private String transId;
    @JacksonXmlProperty(localName = "SIGN_INFO")
    private String sign;//签名

    public BasicResponseMetadata() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestSsn() {
        return requestSsn;
    }

    public void setRequestSsn(String requestSsn) {
        this.requestSsn = requestSsn;
    }

    public String getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(String passwordId) {
        this.passwordId = passwordId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
