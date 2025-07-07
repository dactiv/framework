package com.github.dactiv.framework.allin.pay.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

public enum PayTypeEnum implements NameValueEnum<String> {

    WECHAT_QR("W01", "微信扫码支付"),
    WECHAT_JS("W02", "微信JS支付"),
    ALIPAY_QR("A01", "支付宝扫码支付"),
    ALIPAY_JS("A02", "支付宝JS支付"),
    WECHAT_MINI_PROGRAM("W06", "微信小程序支付"),
    UNION_PAY_QR("U01", "云闪付扫码支付(CSB)"),
    UNION_PAY_JS("U02", "云闪付JS支付"),
    CASHIER_MINI_PROGRAM("W06S", "收银宝小程序收银台"),
    WECHAT_PRE_CONSUMPTION("W11", "微信订单预消费");

    ;

    PayTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    private final String value;

    private final String name;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

}
