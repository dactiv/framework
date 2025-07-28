package com.github.dactiv.framework.citic.domain.body.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.BasicResponseMetadata;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author maurice.chen
 */
public class WithdrawalResponseBody extends BasicResponseMetadata {

    @Serial
    private static final long serialVersionUID = -1630520203254214558L;

    @JacksonXmlProperty(localName = "TRANS_DT")
    private LocalDate transactionDate;

    @JacksonXmlProperty(localName = "TRANS_TM")
    private LocalTime transactionTime;

    @JacksonXmlProperty(localName = "USER_SSN")
    private String transactionNumber;

    @JacksonXmlProperty(localName = "WITH_CHANNEL")
    private String channel;

    public WithdrawalResponseBody() {
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

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
