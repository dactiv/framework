package com.github.dactiv.framework.citic.domain.metadata;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 中信银行公共请求体
 *
 * @author mauric.chen
 */

public class BasicRequestMetadata extends BasicMerchantMetadata {

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

    /**
     * 发起方保留域
     */
    @JacksonXmlProperty(localName = "REQ_RESERVED")
    private String reqReserved;

    public BasicRequestMetadata() {
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
