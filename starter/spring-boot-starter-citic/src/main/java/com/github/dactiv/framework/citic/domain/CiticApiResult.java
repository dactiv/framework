package com.github.dactiv.framework.citic.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ROOT")
public class CiticApiResult<T> {

    @JacksonXmlProperty(localName = "CODE")
    private String code;//外联平台应答码
    @JacksonXmlProperty(localName = "MESSAGE")
    private String message;//外联平台应答码描述
    // 应答数据
    @JacksonXmlProperty(localName = "DATA")
    private T data;

    public CiticApiResult() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
