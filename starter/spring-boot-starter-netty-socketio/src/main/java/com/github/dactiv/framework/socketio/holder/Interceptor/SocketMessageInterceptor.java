package com.github.dactiv.framework.socketio.holder.Interceptor;

import com.github.dactiv.framework.socketio.SocketMessageClient;
import com.github.dactiv.framework.socketio.domain.SocketResult;
import com.github.dactiv.framework.socketio.holder.SocketResultHolder;
import com.github.dactiv.framework.socketio.holder.annotation.SocketMessage;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Objects;

/**
 * spring mvc 拦截器的 socket 结果集持有者实现
 *
 * @author maurice.chen
 */
public class SocketMessageInterceptor implements MethodInterceptor {

    private final SocketMessageClient socketMessageClient;

    public SocketMessageInterceptor(SocketMessageClient socketMessageClient) {
        this.socketMessageClient = socketMessageClient;
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

            socketMessageClient.asyncSendSocketResult(socketResult);

            return returnValue;
        } finally {
            SocketResultHolder.clear();
        }
    }
}
