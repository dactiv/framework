package com.github.dactiv.framework.citic.domain.body.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.dactiv.framework.citic.domain.metadata.BasicRequestMetadata;

import java.io.Serial;

/**
 * @author maurice.chen
 */
public class DownloadFileRequestBody extends BasicRequestMetadata {

    @Serial
    private static final long serialVersionUID = 8772450414047676886L;

    @JacksonXmlProperty(localName = "FILE_NAME")
    private String fileName;

    @JacksonXmlProperty(localName = "FILE_TYPE")
    private String fileType;

    @JacksonXmlProperty(localName = "SETTLE_DT")
    private String settleDate;

    @JacksonXmlProperty(localName = "TRANS_TYPE")
    private String transactionType;

    public DownloadFileRequestBody() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
