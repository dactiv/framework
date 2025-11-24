package com.github.dactiv.framework.socketio.resolver;

import com.corundumstudio.socketio.SocketIOServer;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.security.filter.result.IgnoreOrDesensitizeResultHolder;
import com.github.dactiv.framework.socketio.config.SocketConfig;
import com.github.dactiv.framework.socketio.domain.metadata.AbstractSocketMessageMetadata;
import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息发送者解析器
 *
 * @author maurice.chen
 */
public interface MessageSenderResolver {

    /**
     * 发送消息
     *
     * @param socketMessage  socket 消息
     * @param socketIOServer socket 服务
     */
    void sendMessage(AbstractSocketMessageMetadata<?> socketMessage, SocketIOServer socketIOServer);

    /**
     * 是否支持此消息类型
     *
     * @param socketMessage socket 消息
     *
     * @return true 是，否则 false
     */
    boolean isSupport(AbstractSocketMessageMetadata<?> socketMessage);

    default String postMessageToJson(String event, Object message, SocketConfig config) {
        Map<String, Object> source = Casts.convertValue(message, Casts.MAP_TYPE_REFERENCE);
        List<String> ignoreProperties = config.getIgnoreResultMap().get(event);
        if (CollectionUtils.isNotEmpty(ignoreProperties)) {
            source = Casts.ignoreObjectFieldToMap(message, ignoreProperties);
        }

        List<String> desensitizeProperties = IgnoreOrDesensitizeResultHolder.getDesensitizeProperties();
        if (CollectionUtils.isNotEmpty(desensitizeProperties)) {
            source = Casts.desensitizeObjectFieldToMap(source, desensitizeProperties);
        }
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>(source);

        return SystemException.convertSupplier(() -> Casts.getObjectMapper().writeValueAsString(linkedHashMap));
    }

}
