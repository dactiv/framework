package com.github.dactiv.framework.spring.security.entity.support;

import com.github.dactiv.framework.commons.domain.ExpiredToken;
import com.github.dactiv.framework.spring.security.entity.AccessTokenDetails;
import com.github.dactiv.framework.spring.security.entity.AuditAuthenticationSuccessDetails;

import java.io.Serial;
import java.util.Map;

/**
 * 访问令牌认证名称实现
 *
 * @author maurice.chen
 */
public class AccessTokenAuditAuthenticationSuccessDetails extends AuditAuthenticationSuccessDetails implements AccessTokenDetails {

    @Serial
    private static final long serialVersionUID = -5197874001081052789L;

    private ExpiredToken token;

    public AccessTokenAuditAuthenticationSuccessDetails(Object requestDetails,
                                                        Map<String, Object> metadata,
                                                        ExpiredToken token) {
        super(requestDetails, metadata);
        this.token = token;
    }

    public AccessTokenAuditAuthenticationSuccessDetails(AuditAuthenticationSuccessDetails details, ExpiredToken token) {
        super(details.getRequestDetails(), details.getMetadata());
        this.token = token;
    }

    @Override
    public ExpiredToken getToken() {
        return token;
    }

    @Override
    public void setToken(ExpiredToken token) {
        this.token = token;
    }
}
