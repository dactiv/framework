package com.github.dactiv.framework.socketio.interceptor.support;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.socketio.SocketMessageClient;
import com.github.dactiv.framework.socketio.domain.SocketPrincipal;
import com.github.dactiv.framework.socketio.domain.SocketUserMessage;
import com.github.dactiv.framework.socketio.interceptor.AuthorizationInterceptor;
import com.github.dactiv.framework.spring.security.authentication.token.AuditAuthenticationToken;

import static com.github.dactiv.framework.socketio.SocketUserDetailsAuthentication.CLIENT_DISCONNECT_EVENT_NAME;
import static com.github.dactiv.framework.socketio.SocketUserDetailsAuthentication.LOGGER;

public class SingleEndedConnectionAuthorizationInterceptor implements AuthorizationInterceptor {

    private final SocketMessageClient socketMessageClient;

    public SingleEndedConnectionAuthorizationInterceptor(SocketMessageClient socketMessageClient) {
        this.socketMessageClient = socketMessageClient;
    }

    @Override
    public void hasSocketPrincipal(SocketPrincipal old, AuditAuthenticationToken token) {
        try {

            SocketUserMessage<Object> socketUserDetails = SocketUserMessage.of(
                    old,
                    CLIENT_DISCONNECT_EVENT_NAME,
                    RestResult.ofSuccess("您的账号已在其他客户端登陆，如果非本人操作，请及时修改密码")
            );

            RestResult<Object> result = socketMessageClient.unicast(socketUserDetails);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ID 为 [{}] 的用户在其他设备登录，断开上一次设备的链接，响应结果为:{}", old.getId(), Casts.getObjectMapper().writeValueAsString(result));
            }

        } catch (Exception e) {
            LOGGER.error("登出用户失败", e);
        }
    }
}
