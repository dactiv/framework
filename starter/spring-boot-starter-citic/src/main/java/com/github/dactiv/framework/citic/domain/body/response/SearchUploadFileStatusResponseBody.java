package com.github.dactiv.framework.citic.domain.body.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.BasicResponseMetadata;

import java.io.Serial;

/**
 * @author maurice.chen
 */
public class SearchUploadFileStatusResponseBody extends BasicResponseMetadata {

    @Serial
    private static final long serialVersionUID = 5312541707426453155L;

    @JacksonXmlProperty(localName = "RESULT_CODE")
    private String code;

    @JacksonXmlProperty(localName = "RESULT_MSG")
    private String message;

    @JacksonXmlProperty(localName = "FILE_ST")
    private String status;

    public SearchUploadFileStatusResponseBody() {
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
