package com.github.dactiv.framework.spring.security.audit;

import com.github.dactiv.framework.security.audit.AuditPrincipal;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.io.Serial;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 审计用户参数解析器实现
 *
 * @author maurice.chen
 */
public class AuditPrincipalPointcutAdvisor extends AbstractPointcutAdvisor {

    @Serial
    private static final long serialVersionUID = 1987325943711075941L;

    private final AuditPrincipalMethodInterceptor auditPrincipalMethodInterceptor;

    public AuditPrincipalPointcutAdvisor(AuditPrincipalMethodInterceptor auditPrincipalMethodInterceptor) {
        this.auditPrincipalMethodInterceptor = auditPrincipalMethodInterceptor;
    }

    @Override
    public Pointcut getPointcut() {
        return new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return Arrays.stream(method.getParameterTypes())
                        .anyMatch(AuditPrincipal.class::isAssignableFrom);
            }

        };
    }

    @Override
    public Advice getAdvice() {
        return auditPrincipalMethodInterceptor;
    }


}
