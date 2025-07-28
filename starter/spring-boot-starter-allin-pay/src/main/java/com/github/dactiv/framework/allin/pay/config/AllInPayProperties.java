package com.github.dactiv.framework.allin.pay.config;

import com.github.dactiv.framework.commons.domain.metadata.CloudSecretMetadata;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dactiv.allin-pay")
public class AllInPayProperties {

    public static final String UNIFIED_PAY_API = "/api/access/payInterface/unifiedPay";

    public static final String QUERY_UNIFIED_PAY_API = "/api/access/payInterface/findOrderInfo";

    public static final String REFUND_API = "/api/access/payInterface/refund";

    public static final String BATCH_REFUND_API = "/api/access/payInterface/batchRefund";

    public static final String PAYMENT_IN_QR_CODE_API = "/api/access/payInterface/usePaymentCode";

    public static final String SETTLE_BILL_API = "/api/access/payInterface/getSettleBill";

    public static final String FILE_URL_API = "/api/access/accountStatement/getFileUrl";

    private CloudSecretMetadata secret = new CloudSecretMetadata();

    private String baseUrl = "https://interfacetest.allinpaygx.com";

    private String apiResultCodeField = "status";

    private String apiCodeSuccessValue = "0000";

    private String apiResultErrorCodeField = "errorCode";

    private String apiResultMessageField = "errorMessage";

    private String payTypeParamName = "payType";

    public AllInPayProperties() {
    }

    public CloudSecretMetadata getSecret() {
        return secret;
    }

    public void setSecret(CloudSecretMetadata secret) {
        this.secret = secret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiResultCodeField() {
        return apiResultCodeField;
    }

    public void setApiResultCodeField(String apiResultCodeField) {
        this.apiResultCodeField = apiResultCodeField;
    }

    public String getApiCodeSuccessValue() {
        return apiCodeSuccessValue;
    }

    public void setApiCodeSuccessValue(String apiCodeSuccessValue) {
        this.apiCodeSuccessValue = apiCodeSuccessValue;
    }

    public String getApiResultErrorCodeField() {
        return apiResultErrorCodeField;
    }

    public void setApiResultErrorCodeField(String apiResultErrorCodeField) {
        this.apiResultErrorCodeField = apiResultErrorCodeField;
    }

    public String getApiResultMessageField() {
        return apiResultMessageField;
    }

    public void setApiResultMessageField(String apiResultMessageField) {
        this.apiResultMessageField = apiResultMessageField;
    }

    public String getPayTypeParamName() {
        return payTypeParamName;
    }

    public void setPayTypeParamName(String payTypeParamName) {
        this.payTypeParamName = payTypeParamName;
    }
}
