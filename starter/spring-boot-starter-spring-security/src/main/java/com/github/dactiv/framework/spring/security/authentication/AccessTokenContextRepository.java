package com.github.dactiv.framework.spring.security.authentication;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.TimeProperties;
import com.github.dactiv.framework.commons.id.IdEntity;
import com.github.dactiv.framework.commons.id.number.NumberIdEntity;
import com.github.dactiv.framework.crypto.CipherAlgorithmService;
import com.github.dactiv.framework.crypto.algorithm.Base64;
import com.github.dactiv.framework.crypto.algorithm.ByteSource;
import com.github.dactiv.framework.crypto.algorithm.cipher.CipherService;
import com.github.dactiv.framework.crypto.algorithm.exception.CryptoException;
import com.github.dactiv.framework.security.audit.PluginAuditEvent;
import com.github.dactiv.framework.spring.security.authentication.config.AccessTokenProperties;
import com.github.dactiv.framework.spring.security.authentication.config.SpringSecurityProperties;
import com.github.dactiv.framework.spring.security.authentication.token.*;
import com.github.dactiv.framework.spring.security.entity.MobileUserDetails;
import com.github.dactiv.framework.spring.security.entity.SecurityUserDetails;
import com.github.dactiv.framework.spring.web.device.DeviceUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 访问 token 上下文仓库实现，用于移动端用户明细登陆系统后，返回一个 token， 为无状态的 http 传输中，通过该 token 来完成认证授权等所有工作
 *
 * @author maurice.chen
 */
public class AccessTokenContextRepository extends HttpSessionSecurityContextRepository {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AccessTokenContextRepository.class);

    private final RedissonClient redissonClient;

    private final AccessTokenProperties accessTokenProperties;

    private final SpringSecurityProperties springSecurityProperties;

    private final AntPathRequestMatcher loginRequestMatcher;

    private final CipherAlgorithmService cipherAlgorithmService = new CipherAlgorithmService();

    public AccessTokenContextRepository(RedissonClient redissonClient,
                                        SpringSecurityProperties springSecurityProperties,
                                        AccessTokenProperties accessTokenProperties) {
        this.redissonClient = redissonClient;
        this.springSecurityProperties = springSecurityProperties;
        this.accessTokenProperties = accessTokenProperties;
        this.loginRequestMatcher = new AntPathRequestMatcher(springSecurityProperties.getLoginProcessingUrl(), HttpMethod.POST.name());
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        SecurityContext securityContext = readSecurityContextFromRequest(request);

        if (Objects.isNull(securityContext)) {
            securityContext = super.loadContext(requestResponseHolder);
        }

        return securityContext;
    }

    private SecurityContext readSecurityContextFromRequest(HttpServletRequest request) {
        if (this.loginRequestMatcher.matches(request)) {
            return null;
        }
        String token = request.getHeader(accessTokenProperties.getAccessTokenHeaderName());
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(accessTokenProperties.getAccessTokenParamName());
        }

        if (StringUtils.isEmpty(token)) {
            return null;
        }

        return getSecurityContext(token);
    }

    public SecurityContext getSecurityContext(String token) {
        
        if (StringUtils.isEmpty(token) || !Base64.isBase64(token.getBytes())) {
            return null;
        }

        CipherService cipherService = cipherAlgorithmService.getCipherService(accessTokenProperties.getCipherAlgorithmName());
        byte[] key = Base64.decode(accessTokenProperties.getCryptoKey());

        try {
            ByteSource byteSource = cipherService.decrypt(Base64.decode(token), key);
            String plaintext = new String(byteSource.obtainBytes(), Charset.defaultCharset());

            Map<String, Object> plaintextUserDetail = convertPlaintext(plaintext);
            if (MapUtils.isEmpty(plaintextUserDetail)) {
                return null;
            }

            String type = plaintextUserDetail.getOrDefault(PluginAuditEvent.TYPE_FIELD_NAME, StringUtils.EMPTY).toString();
            Object id = plaintextUserDetail.getOrDefault(IdEntity.ID_FIELD_NAME, StringUtils.EMPTY).toString();

            RBucket<SecurityContext> bucket = getSecurityContextBucket(type, id);
            SecurityContext context = bucket.get();
            if (Objects.isNull(context)) {
                return null;
            }

            SecurityUserDetails userDetails = Casts.cast(context.getAuthentication().getDetails());
            Object tokenValue = userDetails.getMeta().get(accessTokenProperties.getAccessTokenParamName());
            if (Objects.isNull(tokenValue)) {
                return null;
            }

            String existToken = tokenValue.toString();
            if (AccessToken.class.isAssignableFrom(tokenValue.getClass())) {
                AccessToken accessToken = Casts.cast(tokenValue);
                existToken = accessToken.getToken();
            }

            if (!StringUtils.equals(existToken, token)) {
                return null;
            }

            if (userDetails instanceof MobileUserDetails) {
                MobileUserDetails mobileUserDetails = Casts.cast(userDetails);
                Object deviceIdentified = plaintextUserDetail.get(DeviceUtils.REQUEST_DEVICE_IDENTIFIED_PARAM_NAME);
                if (!Objects.equals(deviceIdentified, mobileUserDetails.getDeviceIdentified())) {
                    return null;
                }
            }

            RBucket<ExpiredToken> accessTokenBucket = getAccessTokenBucket(token, accessTokenProperties.getAccessTokenCache());
            if (accessTokenBucket.isExists()) {
                return context;
            }

            TimeProperties expiresTime = accessTokenProperties.getAccessTokenCache().getExpiresTime();
            if (Objects.nonNull(expiresTime)) {
                bucket.expireAsync(expiresTime.toDuration());
            }

            return context;
        } catch (CryptoException e) {
            LOGGER.warn("通过密钥:" + accessTokenProperties.getCryptoKey() + "解密token:" + token + "失败");
        }

        return null;
    }

    public RBucket<SecurityContext> getSecurityContextBucket(String type, Object deviceIdentified) {
        String key = getAccessTokenKey(type, deviceIdentified);
        return redissonClient.getBucket(key, new SerializationCodec());
    }

    public String getAccessTokenKey(String type, Object deviceIdentified) {
        return accessTokenProperties.getAccessTokenCache().getName(type + CacheProperties.DEFAULT_SEPARATOR + deviceIdentified);
    }

    public String generatePlaintextString(SecurityUserDetails userDetails) {
        return generatePlaintextString(userDetails, accessTokenProperties.getCryptoKey());
    }

    public String generatePlaintextString(SecurityUserDetails userDetails, String aesKey) {
        Map<String, Object> json = new LinkedHashMap<>();

        json.put(IdEntity.ID_FIELD_NAME, userDetails.getId());
        json.put(springSecurityProperties.getUsernameParamName(), userDetails.getUsername());
        json.put(PluginAuditEvent.TYPE_FIELD_NAME, userDetails.getType());
        json.put(NumberIdEntity.CREATION_TIME_FIELD_NAME, System.currentTimeMillis());

        if (userDetails instanceof MobileUserDetails) {
            MobileUserDetails mobileUserDetails = Casts.cast(userDetails);
            json.put(DeviceUtils.REQUEST_DEVICE_IDENTIFIED_PARAM_NAME, mobileUserDetails.getDeviceIdentified());
        }

        String plaintext = Casts.writeValueAsString(json);

        CipherService cipherService = cipherAlgorithmService.getCipherService(accessTokenProperties.getCipherAlgorithmName());
        byte[] key = Base64.decode(aesKey);
        ByteSource source = cipherService.encrypt(plaintext.getBytes(Charset.defaultCharset()), key);

        return source.getBase64();
    }

    public Map<String, Object> convertPlaintext(String plaintext) {
        return Casts.readValue(plaintext, Casts.MAP_TYPE_REFERENCE);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        super.saveContext(context, request, response);
        saveRedissonSecurityContext(context, request, response);
    }

    /**
     * 删除缓存
     *
     * @param userDetails 用户明细实现
     */
    public void deleteContext(SecurityUserDetails userDetails) {
        RBucket<SecurityContext> bucket = getSecurityContextBucket(userDetails.getType(), userDetails.getId());
        bucket.deleteAsync();
    }

    /**
     * 删除缓存
     *
     * @param type 用户类型
     * @param id 设备唯一识别
     */
    public void deleteContext(String type, Object id) {
        RBucket<SecurityContext> bucket = getSecurityContextBucket(type, id);
        bucket.deleteAsync();
    }

    protected void saveRedissonSecurityContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        if (Objects.isNull(context.getAuthentication()) || !context.getAuthentication().isAuthenticated()) {
            return;
        }

        Authentication authentication = context.getAuthentication();

        if (authentication instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken rememberMeToken = Casts.cast(authentication);
            if (rememberMeToken.isRememberMe()) {
                return;
            }
        }

        if (authentication instanceof SimpleAuthenticationToken) {
            SimpleAuthenticationToken simpleToken = Casts.cast(authentication);
            if (simpleToken.isRememberMe()) {
                return;
            }
        }

        Object details = authentication.getDetails();
        if (Objects.isNull(details) || !SecurityUserDetails.class.isAssignableFrom(details.getClass())) {
            return;
        }

        SecurityUserDetails userDetails = Casts.cast(details);
        if (MapUtils.isEmpty(userDetails.getMeta())) {
            return ;
        }

        Object accessTokenValue = userDetails.getMeta().get(accessTokenProperties.getAccessTokenParamName());
        if (Objects.isNull(accessTokenValue)) {
            return ;
        }

        TimeProperties timeProperties = accessTokenProperties.getAccessTokenCache().getExpiresTime();
        if (AccessToken.class.isAssignableFrom(accessTokenValue.getClass())){
            ExpiredToken accessToken = Casts.cast(accessTokenValue);
            RBucket<ExpiredToken> accessTokenBucket = getAccessTokenBucket(
                    accessToken.getToken(),
                    accessTokenProperties.getAccessTokenCache()
            );
            accessTokenBucket.setAsync(accessToken);
            timeProperties = accessToken.getExpiresTime();
            if (Objects.nonNull(timeProperties)) {
                accessTokenBucket.expireAsync(timeProperties.toDuration());
            }

            Object refreshTokenValue = userDetails.getMeta().get(accessTokenProperties.getRefreshTokenParamName());
            if (Objects.nonNull(refreshTokenValue) && ExpiredToken.class.isAssignableFrom(refreshTokenValue.getClass())) {
                AccessToken refreshToken = Casts.cast(refreshTokenValue);
                RBucket<ExpiredToken> refreshBucket = getAccessTokenBucket(
                        refreshToken.getToken(),
                        accessTokenProperties.getRefreshTokenCache()
                );
                RefreshToken token = new RefreshToken(refreshToken, accessToken);
                refreshBucket.setAsync(token);
                if (Objects.nonNull(refreshToken.getExpiresTime())) {
                    refreshBucket.expireAsync(token.getExpiresTime().toDuration());
                }
            }
        }

        RBucket<SecurityContext> bucket = getSecurityContextBucket(userDetails.getType(), userDetails.getId());
        bucket.set(context);

        if (Objects.nonNull(timeProperties)) {
            bucket.expireAsync(timeProperties.toDuration());
        }
    }

    private RBucket<ExpiredToken> getAccessTokenBucket(String tokenValue, CacheProperties cacheProperties) {
        String md5 = DigestUtils.md5DigestAsHex(tokenValue.getBytes());
        return redissonClient.getBucket(cacheProperties.getName(md5));
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        boolean containsValue = super.containsContext(request);

        if (!containsValue) {
            SecurityContext context = readSecurityContextFromRequest(request);
            containsValue = Objects.nonNull(context);
        }

        return containsValue;
    }

    public AccessTokenProperties getAccessTokenProperties() {
        return accessTokenProperties;
    }
}
