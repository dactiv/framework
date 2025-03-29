package com.github.dactiv.framework.spring.security.audit;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.security.audit.AuditPrincipal;
import com.github.dactiv.framework.spring.security.authentication.token.AuditAuthenticationToken;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class AuditPrincipalMethodInterceptor implements MethodInterceptor {

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (Objects.isNull(securityContext)) {
            return invocation.proceed();
        }
        Authentication authentication = securityContext.getAuthentication();
        if (Objects.isNull(authentication) || !AuditAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return invocation.proceed();
        }
        AuditAuthenticationToken auditAuthenticationToken = Casts.cast(authentication);
        Object[] args = invocation.getArguments();
        for (Object arg : args) {
            if (arg instanceof AuditPrincipal auditPrincipal) {
                auditPrincipal.setPrincipal(auditAuthenticationToken.getName());
            }
        }

        return invocation.proceed();
    }
}
