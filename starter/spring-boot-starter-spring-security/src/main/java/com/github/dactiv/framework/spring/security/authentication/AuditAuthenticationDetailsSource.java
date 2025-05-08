package com.github.dactiv.framework.spring.security.authentication;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.github.dactiv.framework.spring.security.entity.AuditAuthenticationDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Enumeration;
import java.util.Objects;

public class AuditAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

    private final AuthenticationProperties authenticationProperties;

    public AuditAuthenticationDetailsSource(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(context);
        WebAuthenticationDetails webAuthenticationDetails = super.buildDetails(context);
        String type = context.getParameter(authenticationProperties.getTypeParamName());
        if (StringUtils.isEmpty(type)) {
            type = Objects.toString(context.getHeader(authenticationProperties.getTypeHeaderName()), StringUtils.EMPTY);
        }
        return new AuditAuthenticationDetails(
                webAuthenticationDetails,
                type,
                Casts.castMapToMultiValueMap(context.getParameterMap()),
                servletServerHttpRequest.getHeaders()
        );
    }
}
