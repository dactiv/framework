package com.github.dactiv.framework.idempotent.advisor.concurrent;

import com.github.dactiv.framework.idempotent.annotation.Concurrent;
import com.github.dactiv.framework.idempotent.annotation.ConcurrentElements;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.lang.reflect.Method;

/**
 * 并发处理的切面实现
 *
 * @author maurice
 */
public class ConcurrentPointcutAdvisor extends AbstractPointcutAdvisor {

    @Serial
    private static final long serialVersionUID = -2797648387592489604L;

    private final ConcurrentInterceptor concurrentInterceptor;

    public ConcurrentPointcutAdvisor(ConcurrentInterceptor concurrentInterceptor) {
        this.concurrentInterceptor = concurrentInterceptor;
    }

    @NonNull
    @Override
    public Pointcut getPointcut() {
        return new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
                return method.isAnnotationPresent(Concurrent.class) || method.isAnnotationPresent(ConcurrentElements.class);
            }

        };
    }

    @Override
    @NonNull
    public Advice getAdvice() {
        return concurrentInterceptor;
    }
}
