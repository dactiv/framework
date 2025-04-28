package com.github.dactiv.framework.fadada.domain.body.user;

import java.io.Serial;
import java.io.Serializable;

public class GetUserAuthFreeSignRequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -623312816956289103L;

    private String businessId;
    private String email;

    public GetUserAuthFreeSignRequestBody() {
    }

    public String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
