package com.github.dactiv.framework.spring.security.controller;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.spring.security.authentication.AccessTokenContextRepository;
import com.github.dactiv.framework.spring.security.authentication.token.AccessToken;
import com.github.dactiv.framework.spring.security.authentication.token.RefreshToken;
import com.github.dactiv.framework.spring.security.entity.SecurityUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * token 终端
 *
 * @author maurice.chen
 */
@RestController
public class TokenController {

    private final AccessTokenContextRepository accessTokenContextRepository;

    private final RedissonClient redissonClient;

    public TokenController(AccessTokenContextRepository accessTokenContextRepository,
                           RedissonClient redissonClient) {
        this.accessTokenContextRepository = accessTokenContextRepository;
        this.redissonClient = redissonClient;
    }

    @PostMapping("refreshAccessToken")
    public RestResult<Map<String, Object>> refreshAccessToken(@RequestParam String refreshToken,
                                                              @CurrentSecurityContext SecurityContext securityContext) {
        Object details = securityContext.getAuthentication().getDetails();
        Assert.notNull(details, "当前存在未认证用户,请确保 accessToken 是否未过期或值是否正确。");
        Assert.isTrue(SecurityUserDetails.class.isAssignableFrom(details.getClass()),"当前用户非安全用户明细");

        String refreshMd5 = DigestUtils.md5DigestAsHex(refreshToken.getBytes());
        String refresh = accessTokenContextRepository
                .getAccessTokenProperties()
                .getRefreshTokenCache()
                .getName(refreshMd5);

        RBucket<RefreshToken> refreshTokenBucket = redissonClient.getBucket(refresh);
        Assert.isTrue(refreshTokenBucket.isExists(), "[" + refreshToken + "] 刷新令牌已过期");

        RefreshToken refreshTokenValue = refreshTokenBucket.get();
        Assert.isTrue(!refreshTokenValue.isExpired(), "[" + refreshToken + "] 刷新令牌已过期");

        String tokenMd5 = DigestUtils.md5DigestAsHex(refreshTokenValue.getAccessToken().getToken().getBytes());
        String access = accessTokenContextRepository
                .getAccessTokenProperties()
                .getAccessTokenCache()
                .getName(tokenMd5);
        RBucket<AccessToken> accessTokenBucket = redissonClient.getBucket(access);

        AccessToken redisAccessToken = accessTokenBucket.get();
        Assert.isTrue(!redisAccessToken.isExpired(), "[" + redisAccessToken.getToken() + "] 令牌已过期");

        SecurityUserDetails userDetails = Casts.cast(details);
        Object metaToken = userDetails.getMeta().get(
                accessTokenContextRepository
                        .getAccessTokenProperties()
                        .getAccessTokenParamName()
        );
        AccessToken securityUserAccessToken = Casts.cast(metaToken);
        Assert.isTrue(StringUtils.equals(redisAccessToken.getToken(), securityUserAccessToken.getToken()), "令牌匹配不正确");

        RBucket<SecurityContext> securityContextBucket = accessTokenContextRepository.getSecurityContextBucket(
                userDetails.getType(),
                userDetails.getId()
        );
        Assert.isTrue(securityContextBucket.isExists(), "找不到令牌对应的用户明细");

        securityContextBucket.expireAsync(refreshTokenValue.getAccessToken().getExpiresTime().toDuration());

        redisAccessToken.setCreationTime(new Date());
        accessTokenBucket.setAsync(redisAccessToken);
        if (Objects.nonNull(redisAccessToken.getExpiresTime())) {
            refreshTokenBucket.expireAsync(redisAccessToken.getExpiresTime().toDuration());
        }

        refreshTokenValue.setCreationTime(new Date());
        refreshTokenBucket.setAsync(refreshTokenValue);
        if (Objects.nonNull(refreshTokenValue.getExpiresTime())) {
            refreshTokenBucket.expireAsync(refreshTokenValue.getExpiresTime().toDuration());
        }

        AccessToken newRefreshToken = Casts.of(refreshTokenValue, AccessToken.class);
        userDetails.getMeta().put(accessTokenContextRepository.getAccessTokenProperties().getAccessTokenParamName(), redisAccessToken);
        userDetails.getMeta().put(accessTokenContextRepository.getAccessTokenProperties().getRefreshTokenParamName(), newRefreshToken);

        Map<String, Object> result = new LinkedHashMap<>();

        result.put(accessTokenContextRepository.getAccessTokenProperties().getRefreshTokenParamName(), newRefreshToken);
        result.put(accessTokenContextRepository.getAccessTokenProperties().getAccessTokenParamName(), redisAccessToken);

        return RestResult.ofSuccess("延期 [" + redisAccessToken.getToken() + "] 令牌成功", result);
    }

}
