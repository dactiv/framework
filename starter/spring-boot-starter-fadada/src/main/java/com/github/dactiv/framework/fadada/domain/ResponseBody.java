package com.github.dactiv.framework.fadada.domain;

import java.io.Serial;

public class ResponseBody<T> extends HttpResponseBody {

    @Serial
    private static final long serialVersionUID = 827268477956186457L;

    private String code;
    private String msg;
    private T data;

    public ResponseBody() {
    }

    public boolean isSuccess() {
        return this.getHttpStatusCode() != null && this.getHttpStatusCode().equals(200) && "100000".equals(this.code);
    }

    public ResponseBody(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
