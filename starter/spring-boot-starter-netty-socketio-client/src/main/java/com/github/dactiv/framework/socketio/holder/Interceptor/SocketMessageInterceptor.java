package com.github.dactiv.framework.socketio.holder.Interceptor;

import com.github.dactiv.framework.socketio.SocketClientTemplate;
import com.github.dactiv.framework.socketio.domain.SocketResult;
import com.github.dactiv.framework.socketio.holder.SocketResultHolder;
import com.github.dactiv.framework.socketio.holder.annotation.SocketMessage;
import com.github.dactiv.framework.spring.web.mvc.SpringMvcUtils;
import com.github.dactiv.framework.spring.web.result.RestResponseBodyAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.*;

/**
 * spring mvc 拦截器的 socket 结果集持有者实现
 *
 * @author maurice.chen
 */
public class SocketMessageInterceptor implements MethodInterceptor {

    private final SocketClientTemplate socketClientTemplate;

    public SocketMessageInterceptor(SocketClientTemplate socketClientTemplate) {
        this.socketClientTemplate = socketClientTemplate;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        SocketMessage message = AnnotationUtils.findAnnotation(invocation.getMethod(), SocketMessage.class);

        if (Objects.isNull(message)) {
            return invocation.proceed();
        }

        try {

            Object returnValue = invocation.proceed();

            SocketResult socketResult = SocketResultHolder.get();

            socketClientTemplate.asyncSendSocketResult(socketResult);

            return returnValue;
        } finally {
            SocketResultHolder.clear();
        }
    }
}
