package com.github.dactiv.framework.allin.pay.domain.body.request;

import com.github.dactiv.framework.allin.pay.domain.metadata.BasicVersionRequestMetadata;

import java.io.Serial;

/**
 * 退款请求体
 *
 * @author maurice.chen
 */
public class RefundRequestBody extends BasicVersionRequestMetadata {
    @Serial
    private static final long serialVersionUID = -5534355295585190050L;

    private Integer amount;

    private String orderNo;

    private String oriMerchantOrderNo;

    private String trxid;

    private String merchantOrderNo;

    private String refundType;

    private String remark;

    private String groupNo;

    public RefundRequestBody() {
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }
}
