package com.github.dactiv.framework.wechat.domain.metadata.applet;

import com.github.dactiv.framework.commons.Casts;
import org.apache.commons.collections4.MapUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class PhoneInfoMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 3924633879639719980L;
    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;

    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;

    /**
     * 区号
     */
    private String countryCode;

    /**
     * 数据水印
     */
    private WatermarkMetadata watermark;

    public PhoneInfoMetadata(Map<String, Object> body) {
        this.phoneNumber = body.get("phoneNumber").toString();
        this.purePhoneNumber = body.get("purePhoneNumber").toString();
        this.countryCode = body.get("countryCode").toString();
        Map<String, Object> watermarkData = Casts.cast(body.get("watermark"));
        if (MapUtils.isNotEmpty(watermarkData)) {
            this.watermark = new WatermarkMetadata();
            watermark.setTimestamp(new Date(Casts.cast(watermarkData.get("timestamp"), Long.class)));
            watermark.setAppId(watermarkData.get("appid").toString());
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPurePhoneNumber() {
        return purePhoneNumber;
    }

    public void setPurePhoneNumber(String purePhoneNumber) {
        this.purePhoneNumber = purePhoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public WatermarkMetadata getWatermark() {
        return watermark;
    }

    public void setWatermark(WatermarkMetadata watermark) {
        this.watermark = watermark;
    }
}
