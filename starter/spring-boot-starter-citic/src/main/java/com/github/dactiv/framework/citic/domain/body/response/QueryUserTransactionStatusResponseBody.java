package com.github.dactiv.framework.citic.domain.body.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.SignResponseMetadata;

import java.io.Serial;

/**
 * @author maurice.chen
 */
public class QueryUserTransactionStatusResponseBody extends SignResponseMetadata {
    @Serial
    private static final long serialVersionUID = 8160413411681937898L;

    private String status;

    @JacksonXmlProperty(localName = "TRANS_DATE")
    private String transactionDate;

    @JacksonXmlProperty(localName = "TRANS_TIME")
    private String transactionTime;

    @JacksonXmlProperty(localName = "USER_SSN")
    private String userTransactionSsn;

    @JacksonXmlProperty(localName = "RESULT_CODE")
    private String code;

    @JacksonXmlProperty(localName = "RESULT_MSG")
    private String message;

    public QueryUserTransactionStatusResponseBody() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getUserTransactionSsn() {
        return userTransactionSsn;
    }

    public void setUserTransactionSsn(String userTransactionSsn) {
        this.userTransactionSsn = userTransactionSsn;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
