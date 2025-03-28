package com.github.dactiv.framework.spring.security.entity;

import com.github.dactiv.framework.spring.security.audit.SecurityAuditEventRepositoryInterceptor;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.Serial;

/**
 * 记住我认证明细实现, 添加请求 url 用于在审计中调用什么地址才算审计用户登录使用
 *
 * @see SecurityAuditEventRepositoryInterceptor#preAddHandle(AuditEvent)
 *
 * @author maurice.chen
 */
public class RememberMeAuthenticationDetails extends WebAuthenticationDetails {

    @Serial
    private static final long serialVersionUID = 6969554356974055547L;
    /**
     * 请求地址
     */
    private final String requestUri;

    public RememberMeAuthenticationDetails(String remoteAddress,
                                           String sessionId,
                                           String requestUri) {
        super(remoteAddress, sessionId);
        this.requestUri = requestUri;
    }

    /**
     * 获取请求地址
     *
     * @return 请求地址
     */
    public String getRequestUri() {
        return requestUri;
    }
}
