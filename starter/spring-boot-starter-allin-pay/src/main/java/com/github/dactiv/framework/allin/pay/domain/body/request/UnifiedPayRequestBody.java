package com.github.dactiv.framework.allin.pay.domain.body.request;

import com.github.dactiv.framework.allin.pay.domain.metadata.BasicOrderRequestMetadata;

import java.io.Serial;

public class UnifiedPayRequestBody extends BasicOrderRequestMetadata {

    @Serial
    private static final long serialVersionUID = 2672681383541187192L;

    private String acct;

    private String payType;

    private String subAppid;

    private String chnlstoreid;

    private String goodsTag;

    public UnifiedPayRequestBody() {
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

    public String getSubAppid() {
        return subAppid;
    }

    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
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
}
