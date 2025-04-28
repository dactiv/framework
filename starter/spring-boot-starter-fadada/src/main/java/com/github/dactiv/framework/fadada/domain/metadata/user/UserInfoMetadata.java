package com.github.dactiv.framework.fadada.domain.metadata.user;

import java.io.Serial;
import java.io.Serializable;

public class UserInfoMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -8871884180367729893L;

    private String bankAccountNo;
    private String mobile;

    public UserInfoMetadata() {

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
