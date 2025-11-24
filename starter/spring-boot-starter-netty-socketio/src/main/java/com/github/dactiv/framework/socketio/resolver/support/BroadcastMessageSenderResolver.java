package com.github.dactiv.framework.socketio.resolver.support;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.ClientOperations;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.namespace.Namespace;
import com.github.dactiv.framework.socketio.domain.metadata.AbstractSocketMessageMetadata;
import com.github.dactiv.framework.socketio.domain.metadata.BroadcastMessageMetadata;
import org.apache.commons.lang3.StringUtils;

/**
 * 广播消息发送者
 *
 * @author maurice.chen
 */
public class BroadcastMessageSenderResolver extends AbstractMessageSenderResolver<BroadcastMessageMetadata<?>> {

    @Override
    protected ClientOperations getClientOperations(BroadcastMessageMetadata<?> message, SocketIOServer socketIoServer) {

        BroadcastOperations broadcastOperations = socketIoServer.getRoomOperations(Namespace.DEFAULT_NAME);

        String room = message.getRoom();

        if (StringUtils.isNotBlank(room)) {
            broadcastOperations = socketIoServer.getRoomOperations(room);
        }

        return broadcastOperations;
    }

    @Override
    public boolean isSupport(AbstractSocketMessageMetadata<?> socketMessage) {
        return BroadcastMessageMetadata.class.isAssignableFrom(socketMessage.getClass());
    }
}
