package com.github.dactiv.framework.spring.security.authentication;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.security.entity.SecurityPrincipal;
import com.github.dactiv.framework.spring.security.authentication.token.AuditAuthenticationToken;
import com.github.dactiv.framework.spring.security.authentication.token.RequestAuthenticationToken;
import com.github.dactiv.framework.spring.security.authentication.token.TypeAuthenticationToken;
import com.github.dactiv.framework.spring.security.entity.AuditAuthenticationSuccessDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 带类型的安全用户服务，用于根据不同类型构造出不同 {@link SecurityPrincipal} 内容使用
 *
 * @author maurice.chen
 */
public interface TypeSecurityPrincipalService {

    /**
     * 获取认证用户明细
     *
     * @param token 请求认证 token
     * @return 安全用户接口
     * @throws AuthenticationException 认证错误抛出的异常
     */
    SecurityPrincipal getSecurityPrincipal(TypeAuthenticationToken token) throws AuthenticationException;

    /**
     * 获取用户授权集合
     *
     * @param token     请求认证 token
     * @param principal 安全用户接口
     * @return 用户授权集合
     */
    Collection<GrantedAuthority> getPrincipalGrantedAuthorities(TypeAuthenticationToken token, SecurityPrincipal principal);

    /**
     * 获取支持的用户类型
     *
     * @return 用户类型
     */
    List<String> getType();

    /**
     * 获取密码编码器
     *
     * @return 密码编码器
     */
    PasswordEncoder getPasswordEncoder();

    /**
     * 匹配密码是否正确
     *
     * @param presentedPassword 提交过来的密码
     * @param token             请求认证 token
     * @param principal         spring security 用户实现
     * @return true 是，否则 false
     */
    default boolean matchesPassword(String presentedPassword,
                                    TypeAuthenticationToken token,
                                    SecurityPrincipal principal) {
        return getPasswordEncoder().matches(presentedPassword, principal.getCredentials().toString());
    }

    /**
     * 创建认证成功 token
     *
     * @param principal          当前用户
     * @param token              token 信息
     * @param grantedAuthorities 权限信息
     * @return 新的认证 token
     */
    default AuditAuthenticationToken createSuccessAuthentication(SecurityPrincipal principal,
                                                                 TypeAuthenticationToken token,
                                                                 Collection<? extends GrantedAuthority> grantedAuthorities) {

        AuditAuthenticationToken result = new AuditAuthenticationToken(principal, token, grantedAuthorities);
        result.setAuthenticated(true);
        result.setRememberMe(false);

        AuditAuthenticationSuccessDetails successDetails = getPrincipalDetails(principal, token, grantedAuthorities);
        successDetails.setRemember(result.isRememberMe());
        result.setDetails(successDetails);

        return result;
    }

    default Authentication createOidcUserInfoSuccessAuthentication(SecurityPrincipal principal,
                                                                   TypeAuthenticationToken token,
                                                                   JwtAuthenticationToken jwtAuthenticationToken) {
        AuditAuthenticationToken result = new AuditAuthenticationToken(principal, token, jwtAuthenticationToken.getAuthorities());
        result.setAuthenticated(true);
        result.setRememberMe(false);

        AuditAuthenticationSuccessDetails successDetails = getPrincipalDetails(principal, token, jwtAuthenticationToken.getAuthorities());
        successDetails.setRemember(result.isRememberMe());
        result.setDetails(successDetails);

        return result;
    }

    /**
     * 创建记住我认证成功 token
     *
     * @param principal          当前用户
     * @param token              token 信息
     * @param grantedAuthorities 权限信息
     * @return 新的认证 token
     */
    default AuditAuthenticationToken createRememberMeAuthenticationSuccessToken(SecurityPrincipal principal,
                                                                                TypeAuthenticationToken token,
                                                                                Collection<? extends GrantedAuthority> grantedAuthorities) {

        AuditAuthenticationToken result = new AuditAuthenticationToken(principal, token, grantedAuthorities);
        result.setAuthenticated(true);
        result.setRememberMe(true);

        AuditAuthenticationSuccessDetails successDetails = getPrincipalDetails(principal, token, grantedAuthorities);
        successDetails.setRemember(result.isRememberMe());
        result.setDetails(successDetails);

        return result;
    }

    /**
     * 获取当前用户明细信息
     *
     * @param principal          当前用户
     * @param token              token 信息
     * @param grantedAuthorities 权限信息
     * @return 用户明细信息
     */
    default AuditAuthenticationSuccessDetails getPrincipalDetails(SecurityPrincipal principal,
                                                                  TypeAuthenticationToken token,
                                                                  Collection<? extends GrantedAuthority> grantedAuthorities) {

        if (token instanceof RequestAuthenticationToken requestAuthenticationToken) {
            return new AuditAuthenticationSuccessDetails(token.getDetails(), requestAuthenticationToken.getMetadata());
        }

        return new AuditAuthenticationSuccessDetails(token.getDetails(), new LinkedHashMap<>());
    }

    /**
     * 获取认证缓存配置
     *
     * @param token 请求认证 token
     * @return 缓存配置
     */
    default CacheProperties getAuthenticationCache(TypeAuthenticationToken token) {
        return null;
    }

    /**
     * 获取授权缓存配置
     *
     * @param token     请求认证 token
     * @return 缓存配置
     */
    default CacheProperties getAuthorizationCache(TypeAuthenticationToken token) {
        return null;
    }


    default boolean preSaveSecurityPrincipalCache(TypeAuthenticationToken token, SecurityPrincipal principal) {
        return true;
    }

    default boolean preSaveGrantedAuthoritiesCache(TypeAuthenticationToken token, SecurityPrincipal principal, Collection<GrantedAuthority> grantedAuthorities) {
        return true;
    }
}
