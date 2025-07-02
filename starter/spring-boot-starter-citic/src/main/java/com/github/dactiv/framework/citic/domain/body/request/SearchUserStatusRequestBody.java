package com.github.dactiv.framework.citic.domain.body.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.BasicRequestMetadata;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;

public class SearchUserStatusRequestBody extends BasicRequestMetadata {

    @Serial
    private static final long serialVersionUID = -7816076399964435909L;

    @NotNull
    @JacksonXmlProperty(localName = "USER_ID")
    private String userId;
}
