package com.github.dactiv.framework.fasc.req.user;/**
 * @author xjf
 * @date 2023年09月22日 14:02
 */

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 *
 *
 * @author xjf
 * @date 2023年09月22日 14:02
 */
public class VerifyAuthCodeReq extends BaseReq {

    private String transactionId;
    private String authCode;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
