package com.github.dactiv.framework.socketio.resolver.support;

import com.corundumstudio.socketio.ClientOperations;
import com.corundumstudio.socketio.SocketIOServer;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.socketio.config.SocketConfig;
import com.github.dactiv.framework.socketio.domain.metadata.AbstractSocketMessageMetadata;
import com.github.dactiv.framework.socketio.domain.metadata.MultipleUnicastMessageMetadata;
import com.github.dactiv.framework.socketio.resolver.MessageSenderResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 单数据循环单播 socket 消息发送实现
 *
 * @author maurice.chen
 */
public class MultipleUnicastMessageSender implements MessageSenderResolver {

    public static final Logger LOGGER = LoggerFactory.getLogger(MultipleUnicastMessageSender.class);

    private final SocketConfig config;

    public MultipleUnicastMessageSender(SocketConfig config) {
        this.config = config;
    }

    @Override
    public void sendMessage(AbstractSocketMessageMetadata<?> socketMessage, SocketIOServer socketIoServer) {
        MultipleUnicastMessageMetadata<?> message = Casts.cast(socketMessage);

        List<ClientOperations> clientOperations = message
                .getDeviceIdentifiedList()
                .stream()
                .map(c -> socketIoServer.getClient(UUID.fromString(c)))
                .collect(Collectors.toList());

        String json = postMessageToJson(message.getEvent(), message.getMessage(), config);

        Assert.hasText(json, "推送消息不能为空");

        clientOperations.forEach(c -> AbstractMessageSenderResolver.sendEventMessage(c, socketMessage.getEvent(), json));
        if (!LOGGER.isDebugEnabled()) {
            return ;
        }
        LOGGER.debug(
                "发送消息 [事件类型: {}, 数据: {}}] 到设备 [{}] 成功",
                message.getEvent(),
                json,
                message.getDeviceIdentifiedList()
        );
    }

    @Override
    public boolean isSupport(AbstractSocketMessageMetadata<?> socketMessage) {
        return MultipleUnicastMessageMetadata.class.isAssignableFrom(socketMessage.getClass());
    }
}
