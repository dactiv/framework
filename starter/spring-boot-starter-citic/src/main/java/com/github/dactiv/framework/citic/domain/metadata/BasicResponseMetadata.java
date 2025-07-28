package com.github.dactiv.framework.citic.domain.metadata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author maurice.chen
 */
public class BasicResponseMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 7720848186082816691L;

    /**
     * 应答码
     */
    @JacksonXmlProperty(localName = "RSP_CODE")
    private String code;

    /**
     * 应答码描述
     */
    @JacksonXmlProperty(localName = "RSP_MSG")
    private String message;

    /**
     * 发起方流水号
     */
    @JacksonXmlProperty(localName = "REQ_SSN")
    private String requestSsn;

    /**
     * 动态密码句柄
     */
    @JacksonXmlProperty(localName = "PWDID")
    private String passwordId;

    /**
     * 交易标识
     */
    @JacksonXmlProperty(localName = "TRANS_ID")
    private String transactionId;

    /**
     * 签名
     */
    @JacksonXmlProperty(localName = "SIGN_INFO")
    private String sign;

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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
