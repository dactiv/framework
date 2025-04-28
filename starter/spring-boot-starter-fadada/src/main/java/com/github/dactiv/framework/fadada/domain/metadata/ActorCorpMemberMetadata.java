package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

public class ActorCorpMemberMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 8183829780295790829L;

    private String memberId;
    private String accountName;

    public ActorCorpMemberMetadata() {
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
