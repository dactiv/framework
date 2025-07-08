package com.github.dactiv.framework.allin.pay.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

public class BatchRefundMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 8153617125051609258L;

    private Integer refundAmount;

    private String orderNo;

    private String oriMerchantOrderNo;

    private String trxid;

    private String merchantOrderNo;

    private String refundType;

    private String remark;

    public BatchRefundMetadata() {
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOriMerchantOrderNo() {
        return oriMerchantOrderNo;
    }

    public void setOriMerchantOrderNo(String oriMerchantOrderNo) {
        this.oriMerchantOrderNo = oriMerchantOrderNo;
    }

    public String getTrxid() {
        return trxid;
    }

    public void setTrxid(String trxid) {
        this.trxid = trxid;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
