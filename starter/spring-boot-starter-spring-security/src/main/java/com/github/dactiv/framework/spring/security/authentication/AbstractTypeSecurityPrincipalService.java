package com.github.dactiv.framework.spring.security.authentication;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.github.dactiv.framework.spring.security.authentication.token.TypeAuthenticationToken;

/**
 * 抽象的用户明细服务实现，实现创建认证 token，等部分公用功能
 *
 * @author maurice.chen
 *
 */
public abstract class AbstractTypeSecurityPrincipalService implements TypeSecurityPrincipalService {

    private AuthenticationProperties authenticationProperties;

    public AbstractTypeSecurityPrincipalService() {
    }

    public void setAuthenticationProperties(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    /**
     * 获取配置信息
     *
     * @return 配置信息
     */
    public AuthenticationProperties getAuthenticationProperties() {
        return authenticationProperties;
    }

    @Override
    public CacheProperties getAuthorizationCache(TypeAuthenticationToken token) {
        return CacheProperties.of(
                authenticationProperties.getAuthorizationCache().getName(token.getType()),
                authenticationProperties.getAuthorizationCache().getExpiresTime()
        ) ;
    }

    @Override
    public CacheProperties getAuthenticationCache(TypeAuthenticationToken token) {
        return CacheProperties.of(
                authenticationProperties.getAuthenticationCache().getName(token.getName()),
                authenticationProperties.getAuthenticationCache().getExpiresTime()
        ) ;
    }
}
