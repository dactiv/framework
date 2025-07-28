package com.github.dactiv.framework.allin.pay.domain.body.request;

import com.github.dactiv.framework.allin.pay.domain.metadata.BasicVersionOrderRequestMetadata;

import java.io.Serial;

/**
 * @author maurice.chen
 */
public class SettleBillRequestBody extends BasicVersionOrderRequestMetadata {

    @Serial
    private static final long serialVersionUID = -5302866122679253093L;

    private String dateStr;

    public SettleBillRequestBody() {
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}
