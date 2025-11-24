package com.github.dactiv.framework.socketio.interceptor;

import com.corundumstudio.socketio.HandshakeData;
import com.github.dactiv.framework.commons.id.TypeIdNameMetadata;
import com.github.dactiv.framework.socketio.domain.SocketPrincipal;
import com.github.dactiv.framework.spring.security.authentication.token.AuditAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;

public interface AuthorizationInterceptor {

    default SecurityContext getAuthorizationResult(TypeIdNameMetadata typeIdNameMetadata, HandshakeData handshakeData) {
        return null;
    }

    default void hasSocketPrincipal(SocketPrincipal old, AuditAuthenticationToken token) {

    }
}
