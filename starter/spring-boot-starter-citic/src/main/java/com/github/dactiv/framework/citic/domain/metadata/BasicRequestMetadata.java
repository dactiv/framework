package com.github.dactiv.framework.citic.domain.metadata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 中信银行公共请求体
 *
 * @author mauric.chen
 */
@JacksonXmlRootElement(localName = "ROOT")
public class BasicRequestMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -827083928397470593L;

    public static final String MERCHANT_NO_HEADER_NAME = "merchantNo";

    public static final String TRAN_CODE_HEADER_NAME = "tranCode";

    public static final String SERIAL_NO_HEADER_NAME = "serialNo";

    public static final String TRAN_TIMESTAMP_HEADER_NAME = "tranTmpt";

    public static final String VERSION_HEADER_NAME = "version";

    public static final String TRANS_TIMESTAMP_FORMAT = "yyyy-MM-dd-HH.mm.ss.SSSSSS";

    public static final String REQ_SSN_FORMAT = "yyyyMMddHHmmssSSS";

    public static final SimpleDateFormat TRANS_TIMESTAMP_FORMAT_FORMAT = new SimpleDateFormat(TRANS_TIMESTAMP_FORMAT);

    public static final SimpleDateFormat REQ_SSN_FORMAT_FORMAT = new SimpleDateFormat(REQ_SSN_FORMAT);

    @JacksonXmlProperty(localName = "SIGN_INFO")
    private String sign;

    @JacksonXmlProperty(localName = "TRANS_CODE")
    private String transCode;

    @JacksonXmlProperty(localName = "REQ_SSN")
    private String reqSn;

    @JacksonXmlProperty(localName = "MCHNT_ID")
    private String merchantId; // 商户编号

    @JacksonXmlProperty(localName = "REQ_RESERVED")
    private String reqReserved; // 发起方保留域

    public BasicRequestMetadata() {
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public static String getTransTimestampValue() {
        return TRANS_TIMESTAMP_FORMAT_FORMAT.format(new Date());
    }

    public static String getRequestSsnValue() {
        return REQ_SSN_FORMAT_FORMAT.format(new Date());
    }
}
