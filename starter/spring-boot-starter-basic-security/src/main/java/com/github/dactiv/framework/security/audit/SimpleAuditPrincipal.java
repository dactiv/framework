package com.github.dactiv.framework.security.audit;

/**
 * 简单的审计当事人信息接口实现
 *
 * @author maurice.chen
 */
public class SimpleAuditPrincipal implements AuditPrincipal{

    private String principal;

    public SimpleAuditPrincipal() {
    }

    public SimpleAuditPrincipal(String principal) {
        this.principal = principal;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
