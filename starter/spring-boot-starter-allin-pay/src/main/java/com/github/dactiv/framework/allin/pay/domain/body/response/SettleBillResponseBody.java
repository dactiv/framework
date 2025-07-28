package com.github.dactiv.framework.allin.pay.domain.body.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author maurice.chen
 */
public class SettleBillResponseBody implements Serializable {

    @Serial
    private static final long serialVersionUID = -1908100204700959157L;

    @JsonProperty("clearsplittime")
    private String clearSplitTime;

    @JsonProperty("expectclearday")
    private String expectClearDay;

    @JsonProperty("iscleared")
    private String isCleared;

    @JsonProperty("fee")
    private String fee;

    @JsonProperty("clearamt")
    private String clearAmount;

    @JsonProperty("settfee")
    private String settFee;

    @JsonProperty("acctname")
    private String accountName;

    @JsonProperty("bankname")
    private String bankName;

    @JsonProperty("acctno")
    private String accountNumber;

    public SettleBillResponseBody() {
    }

    public String getClearSplitTime() {
        return clearSplitTime;
    }

    public void setClearSplitTime(String clearSplitTime) {
        this.clearSplitTime = clearSplitTime;
    }

    public String getExpectClearDay() {
        return expectClearDay;
    }

    public void setExpectClearDay(String expectClearDay) {
        this.expectClearDay = expectClearDay;
    }

    public String getIsCleared() {
        return isCleared;
    }

    public void setIsCleared(String isCleared) {
        this.isCleared = isCleared;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getClearAmount() {
        return clearAmount;
    }

    public void setClearAmount(String clearAmount) {
        this.clearAmount = clearAmount;
    }

    public String getSettFee() {
        return settFee;
    }

    public void setSettFee(String settFee) {
        this.settFee = settFee;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
