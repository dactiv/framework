package com.github.dactiv.framework.spring.security.authentication.handler;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.spring.security.entity.SecurityUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 响应 json 数据的认证失败处理实现
 *
 * @author maurice.chen
 */
public class JsonAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final List<JsonAuthenticationSuccessResponse> successResponses;

    private final List<AntPathRequestMatcher> loginRequestMatchers = new LinkedList<>();

    public JsonAuthenticationSuccessHandler(List<JsonAuthenticationSuccessResponse> successResponses) {
        this(successResponses, new LinkedList<>());
    }

    public JsonAuthenticationSuccessHandler(List<JsonAuthenticationSuccessResponse> successResponses,
                                            List<AntPathRequestMatcher> antPathRequestMatchers) {

        this.successResponses = successResponses;

        if (CollectionUtils.isNotEmpty(antPathRequestMatchers)) {
            this.loginRequestMatchers.addAll(antPathRequestMatchers);
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {

        if (loginRequestMatchers.stream().noneMatch(matcher -> matcher.matches(request))) {
            chain.doFilter(request, response);
        } else {
            onAuthenticationSuccess(request, response, authentication);
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException{

        RestResult<Object> result = RestResult.of(HttpStatus.OK.getReasonPhrase());
        if (authentication.getDetails() instanceof SecurityUserDetails) {
            SecurityUserDetails userDetails = Casts.cast(authentication.getDetails());
            SecurityUserDetails returnValue = Casts.of(userDetails, SecurityUserDetails.class);
            result.setData(returnValue);
        } else {
            result.setData(authentication);
        }

        if (CollectionUtils.isNotEmpty(successResponses)) {
            successResponses.forEach(f -> f.setting(result, request));
        }

        if (loginRequestMatchers.stream().anyMatch(matcher -> matcher.matches(request))) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(Casts.writeValueAsString(result));
        }
    }
}
