package com.github.dactiv.framework.citic.domain.body.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author maurice.chen
 */
public class WithdrawalRequestBody extends BasicUserIdRequestBody {

    @Serial
    private static final long serialVersionUID = 2550334570978132100L;

    @NotNull
    @JacksonXmlProperty(localName = "WITH_TYPE")
    private String userType;

    @NotNull
    @JacksonXmlProperty(localName = "BUSS_ID")
    private String businessId;

    @NotNull
    @JacksonXmlProperty(localName = "TRANS_DT")
    private LocalDate transactionDate;

    @NotNull
    @JacksonXmlProperty(localName = "TRANS_TM")
    private LocalTime transactionTime;

    @NotNull
    @JacksonXmlProperty(localName = "FEE_TYPE")
    private String feeType;

    @NotNull
    @JacksonXmlProperty(localName = "WITH_AMT")
    private BigDecimal amount;

    @JacksonXmlProperty(localName = "MEMO")
    private String remark;

    public WithdrawalRequestBody() {
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
