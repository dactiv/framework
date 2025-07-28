package com.github.dactiv.framework.citic.domain.body.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.BasicRequestMetadata;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;

/**
 * @author maurice.chen
 */
public class UploadFileRequestBody extends BasicRequestMetadata {

    @Serial
    private static final long serialVersionUID = -1642224608640621105L;

    @NotNull
    @JacksonXmlProperty(localName = "FILE_TYPE")
    private String fileType;

    @NotNull
    @JacksonXmlProperty(localName = "FILE_COUNT")
    private String lineCount;

    @NotNull
    @JacksonXmlProperty(localName = "FILE_NAME")
    private String filename;

    @NotNull
    @JacksonXmlProperty(localName = "TRANS_TYPE")
    private String transactionType;

    @NotNull
    @JacksonXmlProperty(localName = "FILE_CONTENT")
    private String fileContent;

    public UploadFileRequestBody() {
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLineCount() {
        return lineCount;
    }

    public void setLineCount(String lineCount) {
        this.lineCount = lineCount;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
