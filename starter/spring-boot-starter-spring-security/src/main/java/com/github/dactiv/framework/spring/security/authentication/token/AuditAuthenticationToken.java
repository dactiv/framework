package com.github.dactiv.framework.spring.security.authentication.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.security.entity.SecurityPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

/**
 * 简单的用户认证审计 token
 *
 * @author maurice.chen
 */
public class AuditAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 3747271533448473641L;

    public static final String DETAILS_KEY = "details";

    public static final String AUTHORITIES_KEY = "authorities";

    /**
     * 当前用户
     */
    private final SecurityPrincipal principal;

    /**
     * 最后登录时间
     */
    private final Date lastAuthenticationTime;

    /**
     * 当前用户类型
     */
    private final String type;

    /**
     * 是否记住我认证
     */
    private boolean isRememberMe = false;

    /**
     * 当前用户认证 token
     *
     * @param principal   当前用户
     * @param authorities 授权信息
     */
    public AuditAuthenticationToken(SecurityPrincipal principal,
                                    String type,
                                    Collection<? extends GrantedAuthority> authorities,
                                    Date lastAuthenticationTime) {
        super(authorities);
        this.principal = principal;
        this.type = type;
        this.lastAuthenticationTime = lastAuthenticationTime;
    }

    public AuditAuthenticationToken(SecurityPrincipal principal,
                                    TypeAuthenticationToken token) {
        this(principal, token, new LinkedHashSet<>());
    }

    public AuditAuthenticationToken(SecurityPrincipal principal,
                                    TypeAuthenticationToken token,
                                    Collection<? extends GrantedAuthority> grantedAuthorities) {
        this(principal, token.getType(), grantedAuthorities, new Date());
    }

    @Override
    public Object getCredentials() {
        return principal.getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return getSecurityPrincipal();
    }

    @JsonIgnore
    public SecurityPrincipal getSecurityPrincipal() {
        return principal;
    }

    @Override
    public String getName() {
        return getType() + CacheProperties.DEFAULT_SEPARATOR + principal.getName();
    }

    /**
     * 获取用户类型
     *
     * @return 用户类型
     */
    public String getType() {
        return type;
    }

    /**
     * 获取最后登录时间
     *
     * @return 最后登录时间
     */
    public Date getLastAuthenticationTime() {
        return lastAuthenticationTime;
    }

    /**
     * 是否记住我认证
     *
     * @return true 是，否则 false
     */
    public boolean isRememberMe() {
        return isRememberMe;
    }

    /**
     * 设置是否记住我认证
     *
     * @param rememberMe true 是，否则 false
     */
    public void setRememberMe(boolean rememberMe) {
        isRememberMe = rememberMe;
    }

    /**
     * 获取字符串数组形式的授权信息集合
     *
     * @return 字符串数组形式的授权信息集合
     */
    public Collection<String> getGrantedAuthorities() {
        return getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
