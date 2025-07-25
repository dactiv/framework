package com.github.dactiv.framework.citic.domain.body.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.BasicResponseMetadata;

import java.io.Serial;

public class UserRegistrationResponseBody extends BasicResponseMetadata {

    @Serial
    private static final long serialVersionUID = -9185644851678554525L;

    /**
     * 用户 id
     */
    @JacksonXmlProperty(localName = "USER_ID")
    private String userId;

    @JacksonXmlProperty(localName = "IS_NEED_CHECK")
    private String check;//是否需要审核

    public UserRegistrationResponseBody() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
