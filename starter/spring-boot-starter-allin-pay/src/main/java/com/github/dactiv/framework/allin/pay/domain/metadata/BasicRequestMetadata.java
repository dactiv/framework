package com.github.dactiv.framework.allin.pay.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author maurice.chen
 */
public class BasicRequestMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -3074384801046731289L;

    private String merchantNo;

    private String sign;

    public BasicRequestMetadata() {
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
