package com.github.dactiv.framework.spring.security.authentication;

import com.github.dactiv.framework.spring.security.authentication.config.SpringSecurityProperties;
import com.github.dactiv.framework.spring.security.authentication.token.RequestAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * 抽象的用户明细服务实现，实现创建认证 token，等部分公用功能
 *
 * @author maurice.chen
 *
 */
public abstract class AbstractUserDetailsService implements UserDetailsService {

    private SpringSecurityProperties springSecurityProperties;

    public AbstractUserDetailsService() {
    }

    public AbstractUserDetailsService(SpringSecurityProperties springSecurityProperties) {
        this.springSecurityProperties = springSecurityProperties;
    }

    @Override
    public Authentication createToken(HttpServletRequest request, HttpServletResponse response, String type) {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        username = StringUtils.defaultString(username, StringUtils.EMPTY).trim();
        password = StringUtils.defaultString(password, StringUtils.EMPTY);

        boolean rememberMe = obtainRememberMe(request);
        UsernamePasswordAuthenticationToken usernamePasswordToken = new UsernamePasswordAuthenticationToken(username, password);

        return new RequestAuthenticationToken(request, response, usernamePasswordToken, type, rememberMe);
    }

    /**
     * 获取记住我
     *
     * @param request http servlet request
     *
     * @return true 记住我，否则 false
     */
    protected boolean obtainRememberMe(HttpServletRequest request) {
        return BooleanUtils.toBoolean(request.getParameter(springSecurityProperties.getRememberMe().getParamName()));
    }

    /**
     * 获取登陆账户
     *
     * @param request http servlet request
     *
     * @return 登陆账户
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(springSecurityProperties.getUsernameParamName());
    }

    /**
     * 获取登陆密码
     *
     * @param request http servlet request
     *
     * @return 登陆密码
     */
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(springSecurityProperties.getPasswordParamName());
    }

    /**
     * 获取配置信息
     *
     * @return 配置信息
     */
    public SpringSecurityProperties getSpringSecurityProperties() {
        return springSecurityProperties;
    }

    /**
     * 设置配置信息
     *
     * @param springSecurityProperties 配置信息
     */
    public void setSpringSecurityProperties(SpringSecurityProperties springSecurityProperties) {
        this.springSecurityProperties = springSecurityProperties;
    }
}
