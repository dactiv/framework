package com.github.dactiv.framework.fasc.req.user;/**
 * @author xjf
 * @date 2023年09月22日 14:03
 */

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 *
 *
 * @author xjf
 * @date 2023年09月22日 14:03
 */
public class GetAuthCodeReq extends BaseReq {

    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
