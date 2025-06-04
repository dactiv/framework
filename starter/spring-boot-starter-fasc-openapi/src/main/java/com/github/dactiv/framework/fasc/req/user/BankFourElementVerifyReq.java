package com.github.dactiv.framework.fasc.req.user;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

/**
 * @author zhoufucheng
 * @date 2023/6/28 11:40
 */
public class BankFourElementVerifyReq extends BaseReq {
    private String userName;

    private String userIdentNo;

    private String bankAccountNo;

    private String mobile;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdentNo() {
        return userIdentNo;
    }

    public void setUserIdentNo(String userIdentNo) {
        this.userIdentNo = userIdentNo;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
