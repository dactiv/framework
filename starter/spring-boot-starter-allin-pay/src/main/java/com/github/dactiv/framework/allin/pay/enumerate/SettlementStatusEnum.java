package com.github.dactiv.framework.allin.pay.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

/**
 * @author maurice.chen
 */
public enum SettlementStatusEnum implements NameValueEnum<String> {

    UN_SETTLED("01", "未结算"),
    SETTLING("05", "正在结算中"),
    SETTLEMENT_FAILED("08", "结算失败"),
    SETTLED("09", "已结算"),
    MERGED("10", "已合并"),
    FROZEN("11", "冻结不结算"),
    OTHER_SETTLEMENT("12", "其它方式结算"),
    CUSTOMER_ABANDONED("13", "客户放弃结算"),
    COLLECTED_SETTLEMENT("14", "已归集结算");

    ;

    SettlementStatusEnum(String value, String name) {
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
