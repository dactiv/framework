package com.github.dactiv.framework.socketio;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.commons.TimeProperties;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.commons.id.IdNameMetadata;
import com.github.dactiv.framework.commons.id.TypeIdNameMetadata;
import com.github.dactiv.framework.security.audit.IdAuditEvent;
import com.github.dactiv.framework.socketio.domain.SocketPrincipal;
import com.github.dactiv.framework.socketio.enumerate.ConnectStatus;
import com.github.dactiv.framework.socketio.interceptor.AuthorizationInterceptor;
import com.github.dactiv.framework.spring.security.authentication.AccessTokenContextRepository;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.github.dactiv.framework.spring.security.authentication.token.AuditAuthenticationToken;
import com.github.dactiv.framework.spring.security.entity.AuditAuthenticationSuccessDetails;
import com.github.dactiv.framework.spring.web.device.DeviceUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author maurice.chen
 */
public class SocketUserDetailsAuthentication implements AuthorizationListener, ConnectListener, DisconnectListener {
    public static final Logger LOGGER = LoggerFactory.getLogger(SocketUserDetailsAuthentication.class);
    /**
     * 当 socket 服务主动断开时的通知事件
     */
    public static final String SERVER_DISCONNECT_EVENT_NAME = "socket_server_disconnect";

    public static final String CLIENT_DISCONNECT_EVENT_NAME = "client_disconnect";

    public static final String DEFAULT_IDENTIFIED_HEADER_NAME = "io";

    private final AccessTokenContextRepository accessTokenContextRepository;

    private final SocketProperties socketProperties;

    private final RedissonClient redissonClient;

    private final List<AuthorizationInterceptor> authorizationInterceptors;

    /*private final SocketMessageClient socketMessageClient;*/

    public SocketUserDetailsAuthentication(AccessTokenContextRepository accessTokenContextRepository,
                                           SocketProperties socketProperties,
                                           RedissonClient redissonClient,
                                           List<AuthorizationInterceptor> authorizationInterceptors) {
        this.accessTokenContextRepository = accessTokenContextRepository;
        this.socketProperties = socketProperties;
        this.redissonClient = redissonClient;
        this.authorizationInterceptors = authorizationInterceptors;
    }

    public static TypeIdNameMetadata getTypeIdNameMetadata(HandshakeData handshakeData) {
        String deviceIdentified = handshakeData
                .getSingleUrlParam(DeviceUtils.REQUEST_DEVICE_IDENTIFIED_PARAM_NAME);
        String type = handshakeData
                .getSingleUrlParam(IdAuditEvent.TYPE_FIELD_NAME);
        String username = handshakeData
                .getSingleUrlParam(AuthenticationProperties.SECURITY_FORM_USERNAME_PARAM_NAME);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("socket handshakeData:{}", handshakeData.getUrlParams());
        }

        return TypeIdNameMetadata.of(deviceIdentified, username, type);
    }

    @Override
    public void onConnect(SocketIOClient client) {

        TypeIdNameMetadata typeIdNameMetadata = getTypeIdNameMetadata(client.getHandshakeData());
        SocketPrincipal socketPrincipal = getSocketPrincipal(typeIdNameMetadata);

        if (Objects.isNull(socketPrincipal)) {
            LOGGER.warn("在认证通过后连接 socket 时出现获取用户信息为 null 的情况, deviceIdentified 为:{}", typeIdNameMetadata.getId());
            client.sendEvent(SERVER_DISCONNECT_EVENT_NAME, SystemException.convertSupplier(() -> Casts.getObjectMapper().writeValueAsString(RestResult.of("找不到当前认证的用户"))));
            client.disconnect();
        }

        socketPrincipal.setConnectStatus(ConnectStatus.Connected);
        // 设置最后链接时间
        socketPrincipal.setConnectionTime(new Date());

        saveSocketUserDetails(socketPrincipal);

        if (!LOGGER.isDebugEnabled()) {
            return ;
        }

        LOGGER.debug(
                "设备: {} 建立连接成功, IP 为: {} , 用户为: {} 拥有的房间为: {}" ,
                socketPrincipal.getDeviceIdentified(),
                client.getRemoteAddress().toString(),
                "(" + socketPrincipal.getType() + ") " +socketPrincipal.getUsername(),
                client.getAllRooms()
        );
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        TypeIdNameMetadata typeIdNameMetadata = getTypeIdNameMetadata(client.getHandshakeData());

        SocketPrincipal socketPrincipal = getSocketPrincipal(typeIdNameMetadata);

        if (Objects.isNull(socketPrincipal)) {
            return;
        }

        deleteSocketUserDetails(socketPrincipal);

        if (!LOGGER.isDebugEnabled()) {
            return ;
        }

        LOGGER.debug(
                "IP: {} UUID: {} 设备断开连接, 用户为: {}",
                client.getRemoteAddress().toString(),
                socketPrincipal.getDeviceIdentified(),
                "(" + socketPrincipal.getType() + ") " + socketPrincipal.getUsername()
        );
    }

    public SocketPrincipal getSocketPrincipal(String principal) {
        TypeIdNameMetadata typeIdNameMetadata = IdNameMetadata.ofPrincipalString(principal);
        return getSocketPrincipal(typeIdNameMetadata);
    }

    public SocketPrincipal getSocketPrincipal(TypeIdNameMetadata typeIdNameMetadata) {
        if (StringUtils.isEmpty(typeIdNameMetadata.getType())) {
            return getSocketPrincipalByDeviceIdentified(typeIdNameMetadata.getId());
        } else {
            return getSocketPrincipalBucket(typeIdNameMetadata).get();
        }
    }

    public void saveSocketUserDetails(SocketPrincipal socketPrincipal) {
        TypeIdNameMetadata idNameMetadata = getTypeIdNameMetadata(socketPrincipal);

        RBucket<SocketPrincipal> principalBucket = getSocketPrincipalBucket(idNameMetadata);
        principalBucket.set(socketPrincipal);

        RBucket<SocketPrincipal> deviceIdentifiedBucket = getSocketDeviceIdentifiedBucket(socketPrincipal.getDeviceIdentified());
        deviceIdentifiedBucket.set(socketPrincipal);
        TimeProperties time = socketProperties.getUserCache().getExpiresTime();
        if (Objects.nonNull(time)) {
            deviceIdentifiedBucket.expireAsync(time.toDuration());
            principalBucket.expireAsync(time.toDuration());
        }
    }

    private RBucket<SocketPrincipal> getSocketPrincipalBucket(TypeIdNameMetadata typeIdNameMetadata) {
        String key = typeIdNameMetadata.getType() + CacheProperties.DEFAULT_SEPARATOR + typeIdNameMetadata.getId();
        return redissonClient.getBucket(socketProperties.getUserCache().getName(key));
    }

    public SocketPrincipal getSocketPrincipalByDeviceIdentified(String deviceIdentified) {
        return getSocketDeviceIdentifiedBucket(deviceIdentified).get();
    }

    private RBucket<SocketPrincipal> getSocketDeviceIdentifiedBucket(String deviceIdentified) {
        return redissonClient.getBucket(socketProperties.getUserCache().getName(deviceIdentified));
    }

    public void deleteSocketUserDetails(SocketPrincipal socketPrincipal) {

        TypeIdNameMetadata idNameMetadata = getTypeIdNameMetadata(socketPrincipal);
        getSocketPrincipalBucket(idNameMetadata).deleteAsync();

        getSocketDeviceIdentifiedBucket(socketPrincipal.getDeviceIdentified()).deleteAsync();
    }

    private TypeIdNameMetadata getTypeIdNameMetadata(SocketPrincipal socketPrincipal) {
        return TypeIdNameMetadata.of(
                socketPrincipal.getId().toString(),
                socketPrincipal.getUsername(),
                socketPrincipal.getType()
        );
    }


    @Override
    public AuthorizationResult getAuthorizationResult(HandshakeData data) {

        TypeIdNameMetadata typeIdNameMetadata = getTypeIdNameMetadata(data);

        if (StringUtils.isEmpty(typeIdNameMetadata.getName()) || StringUtils.isEmpty(typeIdNameMetadata.getId())) {
            return AuthorizationResult.FAILED_AUTHORIZATION;
        }
        SecurityContext securityContext = null;
        for (AuthorizationInterceptor interceptor : authorizationInterceptors) {
            securityContext = interceptor.getAuthorizationResult(typeIdNameMetadata, data);
            if (Objects.nonNull(securityContext)) {
                break;
            }
        }


        if (Objects.isNull(securityContext)) {
            securityContext = accessTokenContextRepository.getSecurityContext(typeIdNameMetadata.getName());
        }

        if (Objects.isNull(securityContext)) {
            return AuthorizationResult.FAILED_AUTHORIZATION;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("获取 {} 对应的安全上下文，数据为:{}", typeIdNameMetadata.getName(), securityContext.getAuthentication().getName());
        }

        AuditAuthenticationToken token = Casts.cast(securityContext.getAuthentication());
        SocketPrincipal exist = getSocketPrincipal(token.getName());
        if (Objects.nonNull(exist) && !StringUtils.equals(exist.getDeviceIdentified(), typeIdNameMetadata.getId())) {
            for (AuthorizationInterceptor interceptor : authorizationInterceptors) {
                interceptor.hasSocketPrincipal(exist, token);
            }
        }

        data.getHttpHeaders().add(DEFAULT_IDENTIFIED_HEADER_NAME, typeIdNameMetadata.getId());

        SocketPrincipal socketUserDetails = Casts.of(token.getSecurityPrincipal(), SocketPrincipal.class);
        socketUserDetails.setConnectStatus(ConnectStatus.Connecting);
        socketUserDetails.setDeviceIdentified(typeIdNameMetadata.getId());
        socketUserDetails.setType(token.getType());

        AuditAuthenticationSuccessDetails details = Casts.cast(token.getDetails());
        socketUserDetails.setMetadata(Casts.convertValue(details.getMetadata(), Casts.MAP_TYPE_REFERENCE));

        saveSocketUserDetails(socketUserDetails);

        return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
    }

    public SocketProperties getSocketConfig() {
        return socketProperties;
    }
}
