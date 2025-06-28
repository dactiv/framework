package com.github.dactiv.framework.wechat.service;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.domain.AccessToken;
import com.github.dactiv.framework.commons.domain.metadata.RefreshAccessTokenMetadata;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.idempotent.advisor.concurrent.ConcurrentInterceptor;
import com.github.dactiv.framework.wechat.config.AppletConfig;
import com.github.dactiv.framework.wechat.config.WechatConfig;
import com.github.dactiv.framework.wechat.domain.WechatUserDetails;
import com.github.dactiv.framework.wechat.domain.metadata.applet.PhoneInfoMetadata;
import com.github.dactiv.framework.wechat.domain.metadata.applet.SimpleWechatUserDetailsMetadata;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class WechatAppletService extends WechatBasicService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatAppletService.class);

    private final AppletConfig appletConfig;

    public WechatAppletService(AppletConfig appletConfig,
                               WechatConfig wechatConfig,
                               ConcurrentInterceptor concurrentInterceptor) {
        super(wechatConfig, concurrentInterceptor);
        this.appletConfig = appletConfig;
    }

    @Override
    protected RefreshAccessTokenMetadata getRefreshAccessTokenMetadata() {
        return appletConfig.getAccessToken();
    }

    @Override
    public void afterPropertiesSet() {
        try {
            AccessToken accessToken = refreshAccessToken();
            LOGGER.info("[微信小程序] 当前 token 为: {}, 在: {} 后超时", accessToken.getToken(), accessToken.getExpiresInDateTime());
        } catch (Exception e) {
            LOGGER.error("获取微信访问 token 出错", e);
        }
    }

    public AppletConfig getAppletConfig() {
        return appletConfig;
    }

    /**
     * 获取小程序手机好吗
     *
     * @param code 手机号获取凭证
     * @return 微信手机号码元数据信息
     */
    public PhoneInfoMetadata getPhoneNumber(String code) {
        AccessToken token = getAccessTokenIfCacheNull();
        Map<String, String> body = new LinkedHashMap<>();
        body.put("code", code);
        ResponseEntity<Map<String, Object>> result = getRestTemplate().exchange(
                "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + token.getToken(),
                HttpMethod.POST,
                new HttpEntity<>(body, new LinkedMultiValueMap<>()),
                new ParameterizedTypeReference<>() {
                }
        );

        if (isSuccess(result) && Objects.requireNonNull(result.getBody()).containsKey("phone_info")) {
            return new PhoneInfoMetadata(Casts.cast(result.getBody().get("phone_info")));
        } else {
            throwSystemExceptionIfError(result.getBody());
        }

        return null;
    }

    public WechatUserDetails login(String code) {
        String url = MessageFormat.format("https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code", getAppletConfig().getAccessToken().getSecretId(), getAppletConfig().getAccessToken().getSecretKey(), code);
        ResponseEntity<String> result = getRestTemplate().getForEntity(url, String.class);

        String bodyString = Objects.toString(result.getBody(), StringUtils.EMPTY);
        if (StringUtils.isEmpty(bodyString)) {
            throwSystemExceptionIfError(new LinkedHashMap<>());
        }

        Map<String, Object> body = SystemException.convertSupplier(() -> Casts.getObjectMapper().readValue(result.getBody(), Casts.MAP_TYPE_REFERENCE));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("微信小程序登陆，响应结果为:{}", body);
        }

        if (isSuccess(new ResponseEntity<>(body, result.getHeaders(),result.getStatusCode()))) {
            return SimpleWechatUserDetailsMetadata.of(body);
        } else {
            throwSystemExceptionIfError(body);
        }

        return null;
    }

    /**
     * 创建小程序二维码
     *
     * @param param 参数信息
     */
    public byte[] createAppletQrcode(Map<String, Object> param) {
        AccessToken token = getAccessTokenIfCacheNull();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<byte[]> result = getRestTemplate().exchange(
                "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token.getToken(),
                HttpMethod.POST,
                new HttpEntity<>(param, headers),
                new ParameterizedTypeReference<>() {
                }
        );

        return result.getBody();
    }
}
