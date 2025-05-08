package com.github.dactiv.framework.wechat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

/**
 * 微信配置类
 *
 * @author maurice.chen
 */
@Component
@ConfigurationProperties("dactiv.wechat")
public class WechatConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -2546539338401618009L;

    public static final String DEFAULT_QRCODE_TICKET_PARAM_NAME = "ticket";

    /**
     * 请求状态字段名称
     */
    private String statusCodeFieldName = "errcode";

    /**
     * 请求错误消息字段
     */
    private String statusMessageFieldName = "errmsg";

    /**
     * 请求成功匹配值
     */
    private String successCodeValue = "0";

    /**
     * 成功认证后绑定微信参数名称
     */
    private String successAuthenticationBuildParamName = "wechatCode";

    public WechatConfig() {
    }

    public String getStatusCodeFieldName() {
        return statusCodeFieldName;
    }

    public void setStatusCodeFieldName(String statusCodeFieldName) {
        this.statusCodeFieldName = statusCodeFieldName;
    }

    public String getStatusMessageFieldName() {
        return statusMessageFieldName;
    }

    public void setStatusMessageFieldName(String statusMessageFieldName) {
        this.statusMessageFieldName = statusMessageFieldName;
    }

    public String getSuccessCodeValue() {
        return successCodeValue;
    }

    public void setSuccessCodeValue(String successCodeValue) {
        this.successCodeValue = successCodeValue;
    }

    public String getSuccessAuthenticationBuildParamName() {
        return successAuthenticationBuildParamName;
    }

    public void setSuccessAuthenticationBuildParamName(String successAuthenticationBuildParamName) {
        this.successAuthenticationBuildParamName = successAuthenticationBuildParamName;
    }
}
