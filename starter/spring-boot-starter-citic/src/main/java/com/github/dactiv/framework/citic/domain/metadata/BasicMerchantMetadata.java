package com.github.dactiv.framework.citic.domain.metadata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author maurice.chen
 *
 */
@JacksonXmlRootElement(localName = "ROOT")
public class BasicMerchantMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 861188836379955171L;

    @JacksonXmlProperty(localName = "TRANS_CODE")
    private String transCode;

    @JacksonXmlProperty(localName = "REQ_SSN")
    private String reqSn;

    /**
     * 商户编号
     */
    @JacksonXmlProperty(localName = "MCHNT_ID")
    private String merchantId;

    @JacksonXmlProperty(localName = "SIGN_INFO")
    private String sign;

    public BasicMerchantMetadata() {
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getReqSn() {
        return reqSn;
    }

    public void setReqSn(String reqSn) {
        this.reqSn = reqSn;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
