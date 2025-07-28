package com.github.dactiv.framework.allin.pay.domain.body.request;

import com.github.dactiv.framework.allin.pay.domain.metadata.BasicVersionRequestMetadata;

import java.io.Serial;

public class QueryUnifiedPayRequestBody extends BasicVersionRequestMetadata {
    @Serial
    private static final long serialVersionUID = -1950483859711218175L;

    private String merchantOrderNo;

    private String orderNo;

    private String groupNo;

    private String trxid;

    public QueryUnifiedPayRequestBody() {
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getTrxid() {
        return trxid;
    }

    public void setTrxid(String trxid) {
        this.trxid = trxid;
    }
}
