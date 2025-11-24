package com.github.dactiv.framework.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.socketio.domain.SocketPrincipal;
import com.github.dactiv.framework.socketio.domain.SocketResult;
import com.github.dactiv.framework.socketio.domain.metadata.AbstractSocketMessageMetadata;
import com.github.dactiv.framework.socketio.domain.metadata.BroadcastMessageMetadata;
import com.github.dactiv.framework.socketio.domain.metadata.MultipleUnicastMessageMetadata;
import com.github.dactiv.framework.socketio.domain.metadata.UnicastMessageMetadata;
import com.github.dactiv.framework.socketio.resolver.MessageSenderResolver;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author maurice.chen
 */
public class SocketMessageClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(SocketMessageClient.class);

    public static final String DEFAULT_CHAT_ROOM_JOIN_EVENT_NAME = "CHAT_ROOM_JOIN";

    public static final String DEFAULT_CHAT_ROOM_LEAVE_EVENT_NAME = "CHAT_ROOM_LEAVE";

    private final SocketServerManager socketServerManager;

    private final List<MessageSenderResolver> messageSenderResolvers;

    private final ThreadPoolTaskExecutor taskExecutor;

    public SocketMessageClient(ThreadPoolTaskExecutor taskExecutor,
                               SocketServerManager socketServerManager,
                               List<MessageSenderResolver> messageSenderResolvers) {
        this.socketServerManager = socketServerManager;
        this.messageSenderResolvers = messageSenderResolvers;
        this.taskExecutor = taskExecutor;
    }

    public <T> RestResult<T> multipleUnicast(MultipleUnicastMessageMetadata<T> message) {
        List<RestResult<T>> result = multipleUnicast(List.of(message));
        return CollectionUtils.isEmpty(result) ? null : result.iterator().next();
    }

    public <T> List<RestResult<T>> multipleUnicast(List<MultipleUnicastMessageMetadata<T>> messageList) {

        messageList
                .stream()
                .flatMap(r -> r.toUnicastMessageList().stream())
                .forEach(this::sendMessage);

        return messageList
                .stream()
                .map(r -> RestResult.ofSuccess("单播 socket 成功", r.getMessage().getData()))
                .collect(Collectors.toList());
    }

    public <T> CompletableFuture<RestResult<T>> asyncMultipleUnicast(MultipleUnicastMessageMetadata<T> multipleUnicastMessages) {
        return taskExecutor.submitCompletable(() -> multipleUnicast(multipleUnicastMessages));
    }

    public <T> CompletableFuture<List<RestResult<T>>> asyncMultipleUnicast(List<MultipleUnicastMessageMetadata<T>> multipleUnicastMessages) {
        return taskExecutor.submitCompletable(() -> multipleUnicast(multipleUnicastMessages));
    }

    public <T> RestResult<T> unicast(UnicastMessageMetadata<T> message) {
        List<RestResult<T>> result = unicast(List.of(message));
        return CollectionUtils.isEmpty(result) ? null : result.iterator().next();
    }

    public <T> List<RestResult<T>> unicast(List<UnicastMessageMetadata<T>> messages) {
        return messages
                .stream()
                .peek(this::sendMessage)
                .map(r -> RestResult.ofSuccess("单播 socket 成功", r.getMessage().getData()))
                .collect(Collectors.toList());
    }

    public <T> CompletableFuture<RestResult<T>>  asyncUnicast(UnicastMessageMetadata<T> unicastMessage) {
        return taskExecutor.submitCompletable(() -> unicast(unicastMessage));
    }

    public <T> CompletableFuture<List<RestResult<T>>>  asyncUnicast(List<UnicastMessageMetadata<T>> unicastMessages) {
        return taskExecutor.submitCompletable(() -> unicast(unicastMessages));
    }

    public <T> RestResult<T> broadcast(BroadcastMessageMetadata<T> message) {
        List<RestResult<T>> result = broadcast(List.of(message));
        return CollectionUtils.isEmpty(result) ? null : result.iterator().next();
    }

    public <T> List<RestResult<T>> broadcast(List<BroadcastMessageMetadata<T>> messageList) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("broadcast messageList: {}", SystemException.convertSupplier(() -> Casts.getObjectMapper().writeValueAsString(messageList)));
        }
        return messageList
                .stream()
                .peek(this::sendMessage)
                .map(r -> RestResult.ofSuccess("广播 socket 成功", r.getMessage().getData()))
                .collect(Collectors.toList());
    }

    private List<RestResult<SocketPrincipal>> broadcast(Set<String> rooms, String event, List<SocketPrincipal> socketPrincipals) {
        List<BroadcastMessageMetadata<SocketPrincipal>> messageList = new LinkedList<>();
        for (SocketPrincipal socketPrincipal : socketPrincipals) {
            rooms
                    .stream()
                    .map(room -> BroadcastMessageMetadata.of(room, event, socketPrincipal))
                    .forEach(messageList::add);
        }
        return broadcast(messageList);
    }

    public <T> CompletableFuture<RestResult<T>> asyncBroadcast(BroadcastMessageMetadata<T> broadcastMessage) {
        return taskExecutor.submitCompletable(() -> broadcast(broadcastMessage));
    }

    public <T> CompletableFuture<List<RestResult<T>>> asyncBroadcast(List<BroadcastMessageMetadata<T>> broadcastMessages) {
        return taskExecutor.submitCompletable(() -> broadcast(broadcastMessages));
    }

    public void asyncSendSocketResult(SocketResult result) {

        if (CollectionUtils.isNotEmpty(result.getUnicastMessages())) {
            asyncUnicast(result.getUnicastMessages());
        }

        if (CollectionUtils.isNotEmpty(result.getBroadcastMessages())) {
            asyncBroadcast(result.getBroadcastMessages());
        }

        if (CollectionUtils.isNotEmpty(result.getMultipleUnicastMessages())) {
            asyncMultipleUnicast(result.getMultipleUnicastMessages());
        }
    }

    public <T> CompletableFuture<RestResult<List<String>>> asyncJoinRoom(List<String> deviceIdentifies, Set<String> rooms) {
        return taskExecutor.submitCompletable(() -> joinRoom(deviceIdentifies, rooms));
    }

    public RestResult<List<String>> joinRoom(List<String> deviceIdentifies, Set<String> rooms) {
        List<SocketIOClient> clients = deviceIdentifies.stream().map(socketServerManager::getClient).toList();
        clients.forEach(c -> c.joinRooms(rooms));
        return broadcastRoomOperation(rooms, clients, DEFAULT_CHAT_ROOM_JOIN_EVENT_NAME);
    }

    public CompletableFuture<RestResult<List<String>>> asyncLeaveRoom(List<String> deviceIdentifies, Set<String> rooms) {
        return taskExecutor.submitCompletable(() -> leaveRoom(deviceIdentifies, rooms));
    }

    public RestResult<List<String>> leaveRoom(List<String> deviceIdentifies, Set<String> rooms) {
        List<SocketIOClient> clients = deviceIdentifies.stream().map(socketServerManager::getClient).toList();
        clients.forEach(c -> c.leaveRooms(rooms));
        return broadcastRoomOperation(rooms, clients, DEFAULT_CHAT_ROOM_LEAVE_EVENT_NAME);
    }

    private RestResult<List<String>> broadcastRoomOperation(Set<String> rooms, List<SocketIOClient> clients, String defaultChatRoomLeaveEventName) {
        List<SocketPrincipal> data = clients
                .stream()
                .map(SocketIOClient::getSessionId)
                .map(UUID::toString)
                .map(socketServerManager::getSocketPrincipalByDeviceIdentified)
                .toList();
        broadcast(rooms, defaultChatRoomLeaveEventName, data);

        return RestResult.ofSuccess(clients.stream().map(SocketIOClient::getSessionId).map(UUID::toString).toList());
    }

    /**
     * 发送消息
     *
     * @param message socket 消息
     */
    public <T> void sendMessage(AbstractSocketMessageMetadata<T> message) {
        messageSenderResolvers.stream().filter(m -> m.isSupport(message)).forEach(m -> m.sendMessage(message, socketServerManager.getSocketServer()));
    }
}
