package com.github.dactiv.framework.allin.pay.domain.body;

import com.github.dactiv.framework.allin.pay.domain.metadata.BasicVersionRequestMetadata;
import com.github.dactiv.framework.allin.pay.domain.metadata.BatchRefundMetadata;

import java.io.Serial;
import java.util.List;

public class BatchRefundRequestBody extends BasicVersionRequestMetadata {

    @Serial
    private static final long serialVersionUID = -6186389361346106502L;

    private String notifyUrl;

    private String groupNo;

    private List<BatchRefundMetadata> orders;

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public List<BatchRefundMetadata> getOrders() {
        return orders;
    }

    public void setOrders(List<BatchRefundMetadata> orders) {
        this.orders = orders;
    }
}
