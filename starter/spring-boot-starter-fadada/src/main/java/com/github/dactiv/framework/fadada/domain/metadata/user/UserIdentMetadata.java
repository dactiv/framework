package com.github.dactiv.framework.fadada.domain.metadata.user;

import java.io.Serial;
import java.io.Serializable;

public class UserIdentMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -3001440076203986850L;

    private String userName;
    private String identType;
    private String identNo;

    public UserIdentMetadata() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentType() {
        return identType;
    }

    public void setIdentType(String identType) {
        this.identType = identType;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo;
    }
}
