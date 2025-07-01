package com.github.dactiv.framework.citic.domain.body;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.CiticBasicResponseMetadata;

import java.io.Serial;

public class CiticUserRegistrationResponseBody extends CiticBasicResponseMetadata {

    @Serial
    private static final long serialVersionUID = -9185644851678554525L;

    @JacksonXmlProperty(localName = "USER_ID")
    private String userId;//应答码

    public CiticUserRegistrationResponseBody() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
