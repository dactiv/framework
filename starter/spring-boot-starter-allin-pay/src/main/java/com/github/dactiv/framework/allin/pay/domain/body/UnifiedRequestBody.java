package com.github.dactiv.framework.allin.pay.domain.body;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.dactiv.framework.allin.pay.domain.metadata.BasicRequestMetadata;
import com.github.dactiv.framework.allin.pay.service.AllInPayService;

import java.io.Serial;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UnifiedRequestBody extends BasicRequestMetadata {

    @Serial
    private static final long serialVersionUID = 2672681383541187192L;

    private String storeNo;

    private String merchantOrderNo;

    private Integer amount;

    private String notifyUrl;

    private String backUrl;

    private String note;

    private String orderName;

    private String acct;

    private String payType;

    @JsonFormat(pattern = AllInPayService.DATE_TIME_FORMAT)
    private Date validTime;

    private String subAppid;

    private String remark;

    private String asinfo;

    private String groupNo;

    private List<Map<String, Object>> goods;

    private String chnlstoreid;

    private String goodsTag;

    private String fqnum;

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

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public String getSubAppid() {
        return subAppid;
    }

    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public List<Map<String, Object>> getGoods() {
        return goods;
    }

    public void setGoods(List<Map<String, Object>> goods) {
        this.goods = goods;
    }

    public String getChnlstoreid() {
        return chnlstoreid;
    }

    public void setChnlstoreid(String chnlstoreid) {
        this.chnlstoreid = chnlstoreid;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getFqnum() {
        return fqnum;
    }

    public void setFqnum(String fqnum) {
        this.fqnum = fqnum;
    }
}
