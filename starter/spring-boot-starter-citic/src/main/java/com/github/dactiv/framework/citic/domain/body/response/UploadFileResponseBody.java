package com.github.dactiv.framework.citic.domain.body.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.SignResponseMetadata;

import java.io.Serial;

/**
 * @author maurice.chen
 */
public class UploadFileResponseBody extends SignResponseMetadata {
    @Serial
    private static final long serialVersionUID = -568217927575175415L;

    @JacksonXmlProperty(localName = "FILE_NAME")
    private String fileName;

    @JacksonXmlProperty(localName = "ENCRYPTION_FLAG")
    private String encryption;

    @JacksonXmlProperty(localName = "RESULT_CODE")
    private String code;

    @JacksonXmlProperty(localName = "RESULT_MSG")
    private String message;

    public UploadFileResponseBody() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
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
}
