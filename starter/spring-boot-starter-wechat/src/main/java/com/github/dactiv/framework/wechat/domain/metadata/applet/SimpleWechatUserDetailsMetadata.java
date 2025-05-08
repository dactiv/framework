package com.github.dactiv.framework.wechat.domain.metadata.applet;

import com.github.dactiv.framework.wechat.domain.WechatUserDetails;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.util.Map;

public class SimpleWechatUserDetailsMetadata implements WechatUserDetails {

    @Serial
    private static final long serialVersionUID = 8668979795107201067L;

    public static final String SESSION_KEY_FIELD_NAME = "sessionKey";

    public static final String OPEN_ID_FIELD_NAME = "openId";

    public static final String UNION_ID_FIELD_NAME = "unionId";

    /**
     * session key
     */
    private String sessionKey;

    /**
     * 用户唯一标识
     */
    private String openId;

    /**
     * 用户在开放平台的唯一标识符
     */
    private String unionId;

    public SimpleWechatUserDetailsMetadata() {
    }

    @Override
    public String getSessionKey() {
        return sessionKey;
    }

    @Override
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public String getOpenId() {
        return openId;
    }

    @Override
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String getUnionId() {
        return unionId;
    }

    @Override
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public static SimpleWechatUserDetailsMetadata of(Map<String, Object> map) {
        SimpleWechatUserDetailsMetadata meta = new SimpleWechatUserDetailsMetadata();

        meta.setSessionKey(map.get("session_key").toString());
        meta.setOpenId(map.get("openid").toString());

        meta.setUnionId(map.getOrDefault("unionid", StringUtils.EMPTY).toString());

        return meta;
    }
}
