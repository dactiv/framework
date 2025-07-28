package com.github.dactiv.framework.allin.pay.domain.body.request;

import com.github.dactiv.framework.allin.pay.domain.metadata.BasicRequestMetadata;

import java.io.Serial;

/**
 * @author maurice.chen
 */
public class GetFileUrlRequestBody extends BasicRequestMetadata {

    @Serial
    private static final long serialVersionUID = 712172247419667328L;

    private String date;

    private String randomNumber;

    public GetFileUrlRequestBody() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }
}
