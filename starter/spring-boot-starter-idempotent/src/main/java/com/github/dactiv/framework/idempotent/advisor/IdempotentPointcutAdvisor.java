package com.github.dactiv.framework.idempotent.advisor;

import com.github.dactiv.framework.idempotent.annotation.Idempotent;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.lang.reflect.Method;

/**
 * aop 的幂等性切面处理实现
 *
 * @author maurice
 */
public class IdempotentPointcutAdvisor extends AbstractPointcutAdvisor {

    @Serial
    private static final long serialVersionUID = -2973618152809395856L;

    private final IdempotentInterceptor idempotentInterceptor;

    public IdempotentPointcutAdvisor(IdempotentInterceptor idempotentInterceptor) {
        this.idempotentInterceptor = idempotentInterceptor;
    }

    @NonNull
    @Override
    public Pointcut getPointcut() {
        return new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
                return method.isAnnotationPresent(Idempotent.class);
            }

        };
    }

    @NonNull
    @Override
    public Advice getAdvice() {
        return idempotentInterceptor;
    }
}
