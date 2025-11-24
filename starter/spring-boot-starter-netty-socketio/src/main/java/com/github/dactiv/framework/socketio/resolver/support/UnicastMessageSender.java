package com.github.dactiv.framework.socketio.resolver.support;

import com.corundumstudio.socketio.ClientOperations;
import com.corundumstudio.socketio.SocketIOServer;
import com.github.dactiv.framework.socketio.domain.metadata.AbstractSocketMessageMetadata;
import com.github.dactiv.framework.socketio.domain.metadata.UnicastMessageMetadata;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * 单播消息发送者
 *
 * @author maurice.chen
 */
public class UnicastMessageSender extends AbstractMessageSenderResolver<UnicastMessageMetadata<?>> {

    public static final Logger LOGGER = LoggerFactory.getLogger(UnicastMessageSender.class);

    @Override
    public boolean isSupport(AbstractSocketMessageMetadata<?> socketMessage) {
        return UnicastMessageMetadata.class.isAssignableFrom(socketMessage.getClass());
    }

    @Override
    protected ClientOperations getClientOperations(UnicastMessageMetadata<?> message, SocketIOServer socketIoServer) {

        String deviceIdentified = message.getDeviceIdentified();

        if (StringUtils.isNotBlank(deviceIdentified)) {
            return socketIoServer.getClient(UUID.fromString(deviceIdentified));
        }

        return null;
    }

    @Override
    protected void afterSetting(ClientOperations clientOperations, UnicastMessageMetadata<?> message, String json) {
        if (!LOGGER.isDebugEnabled()) {
            return ;
        }
        LOGGER.debug(
                "发送消息 [事件类型: {}, 数据: {}}] 到设备 [{}] 成功",
                message.getEvent(),
                json,
                message.getDeviceIdentified()
        );
    }
}
