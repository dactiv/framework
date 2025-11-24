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
import com.github.dactiv.framework.socketio.config.SocketConfig;
import com.github.dactiv.framework.socketio.domain.SocketPrincipal;
import com.github.dactiv.framework.socketio.domain.SocketUserMessage;
import com.github.dactiv.framework.socketio.enumerate.ConnectStatus;
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

    private final SocketConfig socketConfig;

    private final RedissonClient redissonClient;

    private final SocketMessageClient socketMessageClient;

    public SocketUserDetailsAuthentication(AccessTokenContextRepository accessTokenContextRepository,
                                           SocketConfig socketConfig,
                                           RedissonClient redissonClient,
                                           SocketMessageClient socketMessageClient) {
        this.accessTokenContextRepository = accessTokenContextRepository;
        this.socketConfig = socketConfig;
        this.redissonClient = redissonClient;
        this.socketMessageClient = socketMessageClient;
    }

    private TypeIdNameMetadata getTypeIdNameMetadata(HandshakeData handshakeData) {
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

    /**
     * 断开链接
     *
     * @param details socket 用户明细实现
     */
    private void disconnect(SocketPrincipal details) {
        try {

            SocketUserMessage<Object> socketUserDetails = SocketUserMessage.of(
                    details,
                    CLIENT_DISCONNECT_EVENT_NAME,
                    RestResult.ofSuccess("您的账号已在其他客户端登陆，如果非本人操作，请及时修改密码")
            );

            List<RestResult<Object>> result = socketMessageClient.unicast(List.of(socketUserDetails));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ID 为 [{}] 的用户在其他设备登录，断开上一次设备的链接，响应结果为:{}", details.getId(), result);
            }

        } catch (Exception e) {
            LOGGER.error("登出用户失败", e);
        }
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
        TimeProperties time = socketConfig.getUserCache().getExpiresTime();
        if (Objects.nonNull(time)) {
            deviceIdentifiedBucket.expireAsync(time.toDuration());
            principalBucket.expireAsync(time.toDuration());
        }
    }

    private RBucket<SocketPrincipal> getSocketPrincipalBucket(TypeIdNameMetadata typeIdNameMetadata) {
        String key = typeIdNameMetadata.getType() + CacheProperties.DEFAULT_SEPARATOR + typeIdNameMetadata.getId();
        return redissonClient.getBucket(socketConfig.getUserCache().getName(key));
    }

    public SocketPrincipal getSocketPrincipalByDeviceIdentified(String deviceIdentified) {
        return getSocketDeviceIdentifiedBucket(deviceIdentified).get();
    }

    private RBucket<SocketPrincipal> getSocketDeviceIdentifiedBucket(String deviceIdentified) {
        return redissonClient.getBucket(socketConfig.getUserCache().getName(deviceIdentified));
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

        /*SecurityContext securityContext;
        if (StringUtils.isNotEmpty(typeIdNameMetadata.getType()) && ResourceSourceEnum.CONSOLE_SOURCES.contains(typeIdNameMetadata.getType())) {
            String cookie = data.getHttpHeaders().get(HttpHeaders.COOKIE);
            List<String> cookieValue = Arrays.stream(StringUtils.splitByWholeSeparator(cookie, Casts.SEMICOLON)).map(StringUtils::trim).toList();

            String sessionValue = cookieValue
                    .stream()
                    .filter(s -> StringUtils.startsWith(s, HeaderWebSessionIdResolver.DEFAULT_HEADER_NAME))
                    .findFirst()
                    .orElse(StringUtils.EMPTY);
            String sessionId = Base64.decodeToString(StringUtils.substringAfter(sessionValue, Casts.EQ));
            Session session = sessionRepository.findById(sessionId);

            securityContext = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            typeIdNameMetadata.setId(sessionId);
        } else {*/
        SecurityContext securityContext = accessTokenContextRepository.getSecurityContext(typeIdNameMetadata.getName());
        /*}*/

        if (Objects.isNull(securityContext)) {
            return AuthorizationResult.FAILED_AUTHORIZATION;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("获取 {} 对应的安全上下文，数据为:{}", typeIdNameMetadata.getName(), securityContext.getAuthentication().getName());
        }

        AuditAuthenticationToken token = Casts.cast(securityContext.getAuthentication());
        SocketPrincipal exist = getSocketPrincipal(token.getName());
        if (Objects.nonNull(exist) && !StringUtils.equals(exist.getDeviceIdentified(), typeIdNameMetadata.getId())) {
            this.disconnect(exist);
        }

        data.getHttpHeaders().add(DEFAULT_IDENTIFIED_HEADER_NAME, typeIdNameMetadata.getId());

        SocketPrincipal socketUserDetails = Casts.of(token.getSecurityPrincipal(), SocketPrincipal.class);
        socketUserDetails.setConnectStatus(ConnectStatus.Connecting);
        // 设置 socket server 的 id 地址和端口
        /*socketUserDetails.setSocketServerIp(discoveryProperties.getIp());*/
        //socketUserDetails.setPort(socketConfig.getPort());
        socketUserDetails.setDeviceIdentified(typeIdNameMetadata.getId());
        socketUserDetails.setType(token.getType());

        AuditAuthenticationSuccessDetails details = Casts.cast(token.getDetails());
        socketUserDetails.setMetadata(Casts.convertValue(details.getMetadata(), Casts.MAP_TYPE_REFERENCE));

        saveSocketUserDetails(socketUserDetails);

        return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
    }

    public SocketConfig getSocketConfig() {
        return socketConfig;
    }
}
