package com.github.dactiv.framework.socketio;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.socketio.domain.ReturnValueSocketResult;
import com.github.dactiv.framework.socketio.domain.SocketResult;
import com.github.dactiv.framework.spring.web.SpringWebMvcProperties;
import com.github.dactiv.framework.spring.web.result.RestResponseBodyAdvice;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Objects;

/**
 * socket 结果集响应处理器
 *
 * @author maurice
 */
@ControllerAdvice
public class SocketResultResponseBodyAdvice extends RestResponseBodyAdvice {

    private final SocketMessageClient socketMessageClient;

    public SocketResultResponseBodyAdvice(SpringWebMvcProperties properties, SocketMessageClient socketMessageClient) {
        super(properties);
        this.socketMessageClient = socketMessageClient;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        Object returnValue = body;

        if (Objects.nonNull(body) && SocketResult.class.isAssignableFrom(body.getClass())) {

            SocketResult result = Casts.cast(body);
            socketMessageClient.asyncSendSocketResult(result);

            if (ReturnValueSocketResult.class.isAssignableFrom(result.getClass())) {

                ReturnValueSocketResult<?> returnValueSocketResult = Casts.cast(result);

                returnValue = returnValueSocketResult.getReturnValue();
            }
        }

        return super.beforeBodyWrite(returnValue, returnType, selectedContentType, selectedConverterType, request, response);
    }

}
