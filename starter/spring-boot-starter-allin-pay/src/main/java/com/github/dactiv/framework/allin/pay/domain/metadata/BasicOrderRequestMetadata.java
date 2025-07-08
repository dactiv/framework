package com.github.dactiv.framework.allin.pay.domain.metadata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.dactiv.framework.allin.pay.service.AllInPayService;

import java.io.Serial;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BasicOrderRequestMetadata extends BasicRequestMetadata{

    @Serial
    private static final long serialVersionUID = 1867322369882303765L;

    private String storeNo;

    private String merchantOrderNo;

    private Integer amount;

    private String notifyUrl;

    private String backUrl;

    private String note;

    private String orderName;

    @JsonFormat(pattern = AllInPayService.DATE_TIME_FORMAT)
    private Date validTime;

    private String remark;

    private List<Map<String, Object>> goods = new LinkedList<>();

    private String fqnum;

    private String asinfo;

    private String groupNo;

    public BasicOrderRequestMetadata() {
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Map<String, Object>> getGoods() {
        return goods;
    }

    public void setGoods(List<Map<String, Object>> goods) {
        this.goods = goods;
    }

    public String getFqnum() {
        return fqnum;
    }

    public void setFqnum(String fqnum) {
        this.fqnum = fqnum;
    }

    public String getAsinfo() {
        return asinfo;
    }

    public void setAsinfo(String asinfo) {
        this.asinfo = asinfo;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }
}
