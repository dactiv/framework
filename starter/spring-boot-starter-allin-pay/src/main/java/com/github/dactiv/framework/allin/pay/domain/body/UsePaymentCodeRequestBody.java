package com.github.dactiv.framework.allin.pay.domain.body;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.dactiv.framework.allin.pay.domain.metadata.BasicVersionOrderRequestMetadata;
import com.github.dactiv.framework.allin.pay.service.AllInPayService;

import java.io.Serial;
import java.util.Date;

public class UsePaymentCodeRequestBody extends BasicVersionOrderRequestMetadata {

    @Serial
    private static final long serialVersionUID = 3541609112459058591L;

    @JsonFormat(pattern = AllInPayService.DATE_TIME_FORMAT)
    private Date payTime = new Date();

    public UsePaymentCodeRequestBody() {
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
