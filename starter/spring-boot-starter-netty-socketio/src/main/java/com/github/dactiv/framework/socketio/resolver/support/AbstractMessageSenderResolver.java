package com.github.dactiv.framework.socketio.resolver.support;

import com.corundumstudio.socketio.ClientOperations;
import com.corundumstudio.socketio.SocketIOServer;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.socketio.SocketUserDetailsAuthentication;
import com.github.dactiv.framework.socketio.config.SocketConfig;
import com.github.dactiv.framework.socketio.domain.metadata.AbstractSocketMessageMetadata;
import com.github.dactiv.framework.socketio.resolver.MessageSenderResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * 抽象的消息附送实现
 *
 * @param <T> 继承与 {@link AbstractSocketMessageMetadata} 的子类
 */
public abstract class AbstractMessageSenderResolver<T extends AbstractSocketMessageMetadata<?>> implements MessageSenderResolver {

    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageSenderResolver.class);

    private SocketConfig config;

    @Autowired
    public void setConfig(SocketConfig config) {
        this.config = config;
    }

    @Override
    public void sendMessage(AbstractSocketMessageMetadata<?> socketMessage, SocketIOServer socketIoServer) {

        Assert.hasText(socketMessage.getEvent(), "推送事件不能为空");

        T message = Casts.cast(socketMessage);

        ClientOperations clientOperations = getClientOperations(message, socketIoServer);

        if (Objects.isNull(clientOperations)) {
            LOGGER.warn("找不到 [{}] 的客户端操作", socketIoServer);
            return;
        }

        String json = postMessageToJson(message.getEvent(), message.getMessage(), config);

        Assert.hasText(json, "推送消息不能为空");

        sendEventMessage(clientOperations, message.getEvent(), json);

        afterSetting(clientOperations, message, json);
    }

    /**
     * 后置设置
     *
     * @param clientOperations 客户端操作
     * @param message          继承与 {@link AbstractSocketMessageMetadata} 的子类
     * @param json             {@link AbstractSocketMessageMetadata#getMessage()} 的 json 字符串
     */
    protected void afterSetting(ClientOperations clientOperations, T message, String json) {

    }

    /**
     * 获取客户端操作类
     *
     * @param message 继承与 {@link AbstractSocketMessageMetadata} 的子类
     *
     * @return 客户端操作类
     */
    protected abstract ClientOperations getClientOperations(T message, SocketIOServer socketIOServer);

    /**
     * 发送事件消息
     *
     * @param client  客户端
     * @param message 消息
     */
    public static void sendEventMessage(ClientOperations client, String event, String message) {
        client.sendEvent(event, message);

        if (SocketUserDetailsAuthentication.SERVER_DISCONNECT_EVENT_NAME.equals(event)) {
            client.disconnect();
        }
    }
}
