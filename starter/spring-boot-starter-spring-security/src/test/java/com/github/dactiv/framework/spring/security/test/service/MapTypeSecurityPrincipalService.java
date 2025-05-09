package com.github.dactiv.framework.spring.security.test.service;

import com.github.dactiv.framework.security.entity.SecurityPrincipal;
import com.github.dactiv.framework.security.entity.support.SimpleSecurityPrincipal;
import com.github.dactiv.framework.spring.security.authentication.AbstractTypeSecurityPrincipalService;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.github.dactiv.framework.spring.security.authentication.oidc.OidcUserInfoAuthenticationResolver;
import com.github.dactiv.framework.spring.security.authentication.token.AuditAuthenticationToken;
import com.github.dactiv.framework.spring.security.authentication.token.TypeAuthenticationToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MapTypeSecurityPrincipalService extends AbstractTypeSecurityPrincipalService implements InitializingBean, OidcUserInfoAuthenticationResolver {

    private static final Map<String, SimpleSecurityPrincipal> USER_DETAILS = Collections.synchronizedMap(new HashMap<>());

    private final PasswordEncoder passwordEncoder;

    public MapTypeSecurityPrincipalService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationProperties(AuthenticationProperties authenticationProperties) {
        super.setAuthenticationProperties(authenticationProperties);
    }

    @Override
    public void afterPropertiesSet()  {
        USER_DETAILS.put("test", new SimpleSecurityPrincipal(1, getPasswordEncoder().encode("123456"), "test"));
        USER_DETAILS.put("admin", new SimpleSecurityPrincipal(2, getPasswordEncoder().encode("123456"), "admin"));
    }

    @Override
    public SecurityPrincipal getSecurityPrincipal(TypeAuthenticationToken token) throws AuthenticationException {
        return USER_DETAILS.get(token.getPrincipal().toString());
    }

    @Override
    public Collection<GrantedAuthority> getPrincipalGrantedAuthorities(TypeAuthenticationToken token, SecurityPrincipal principal) {
        Collection<GrantedAuthority> result = new LinkedHashSet<>();
        result.add(new SimpleGrantedAuthority("perms[operate]"));
        return result;
    }

    @Override
    public List<String> getType() {
        return Collections.singletonList("test");
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Override
    public boolean isSupport(String type) {
        return getType().contains(type);
    }

    @Override
    public OidcUserInfo mappingOidcUserInfoClaims(OAuth2Authorization oAuth2Authorization, Map<String, Object> claims, AuditAuthenticationToken authenticationToken) {
        if (oAuth2Authorization.getAuthorizedScopes().contains(OidcScopes.PROFILE)) {
            claims.put(OidcScopes.PROFILE, authenticationToken.getName());
        }
        return new OidcUserInfo(claims);
    }
}
