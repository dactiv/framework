package com.github.dactiv.framework.socketio.domain;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.commons.id.IdEntity;
import com.github.dactiv.framework.socketio.domain.metadata.*;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * socket 结果集
 *
 * @author maurice.chen
 */
public class SocketResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 7253166403465776076L;

    /**
     * 如果存在相同的事件是否替换原有的值:true 是, 否则 false
     */
    private boolean removeIfExist = true;

    /**
     * 单播 socket 消息集合
     */
    private List<UnicastMessageMetadata<?>> unicastMessages = new LinkedList<>();

    /**
     * 同一个结果集播放给多哥用户的消息集合
     */
    private List<MultipleUnicastMessageMetadata<?>> multipleUnicastMessages = new LinkedList<>();

    /**
     * 多播消息集合
     */
    private List<BroadcastMessageMetadata<?>> broadcastMessages = new LinkedList<>();

    /**
     * 添加广播消息
     *
     * @param event   事件
     * @param message 消息对象
     */
    public void addBroadcastSocketMessage(String event, Object message) {
        addBroadcastSocketMessage(null, event, message);
    }

    /**
     * 添加广播结果集
     *
     * @param room    房间名称
     * @param event   事件
     * @param message 消息对象
     */
    public void addBroadcastSocketMessage(String room, String event, Object message) {
        addBroadcastSocketMessage(BroadcastMessageMetadata.of(room, event, RestResult.ofSuccess(message)));
    }

    /**
     * 添加广播结果集
     *
     * @param broadcastMessage 多播消息
     */
    public void addBroadcastSocketMessage(BroadcastMessageMetadata<?> broadcastMessage) {
        removeIfExist(broadcastMessage, getBroadcastMessages());
        getBroadcastMessages().add(broadcastMessage);
    }

    /**
     * 添加同一个结果集但单播给多个用户的消息结果集
     *
     * @param deviceIdentifiedList 设备集合
     * @param message              消息对象
     */
    public void addMultipleUnicastMessage(List<String> deviceIdentifiedList, String event, Object message) {
        addMultipleUnicastMessage(MultipleUnicastMessageMetadata.of(deviceIdentifiedList, event, RestResult.ofSuccess(message)));
    }

    /**
     * 添加同一个结果集但单播给多个用户的消息结果集
     *
     * @param multipleUnicastMessage 单数据循环单播 socket 消息
     */
    public void addMultipleUnicastMessage(MultipleUnicastMessageMetadata<?> multipleUnicastMessage) {
        removeIfExist(multipleUnicastMessage, getMultipleUnicastMessages());
        getMultipleUnicastMessages().add(multipleUnicastMessage);
    }

    /**
     * 添加单播消息
     *
     * @param deviceIdentified 设备唯一识别
     * @param event            事件
     * @param message          消息对象
     */
    public void addUnicastMessage(String deviceIdentified, String event, Object message) {

        if (Objects.isNull(deviceIdentified)) {
            return;
        }

        addUnicastMessage(UnicastMessageMetadata.of(deviceIdentified, event, RestResult.ofSuccess(message)));
    }

    /**
     * 添加单播消息
     *
     * @param unicastMessage 单播 socket 消息对象
     */
    public void addUnicastMessage(UnicastMessageMetadata<?> unicastMessage) {
        removeIfExist(unicastMessage, getUnicastMessages());
        getUnicastMessages().add(unicastMessage);
    }

    /**
     * 添加单播通知
     *
     * @param deviceIdentified 设备唯一识别
     * @param message          消息内容
     * @param type             用户通知类型
     */
    public void addUnicastMessageNotice(String deviceIdentified, String message, String type) {

        addUnicastMessage(
                deviceIdentified,
                UserNoticeMetadata.SYSTEM_USER_NOTICE_EVENT_NAME,
                UserNoticeMetadata.of(null, message, type)
        );
    }

    /**
     * 添加单播通知
     *
     * @param deviceIdentified 设备唯一识别
     * @param title            标题
     * @param message          消息内容
     * @param type             用户通知类型
     */
    public void addUnicastMessageNotice(String deviceIdentified, String title, String message, String type) {
        addUnicastMessage(
                deviceIdentified,
                UserNoticeMetadata.SYSTEM_USER_NOTICE_EVENT_NAME,
                UserNoticeMetadata.of(title, message, type)
        );
    }

    /**
     * 添加多播用户通知
     *
     * @param message 消息内容
     * @param type    用户通知类型
     */
    public void addBroadcastSocketUserNotice(String message, String type) {
        addBroadcastSocketMessage(UserNoticeMetadata.SYSTEM_USER_NOTICE_EVENT_NAME, UserNoticeMetadata.of(null, message, type));
    }

    /**
     * 添加多播用户通知
     *
     * @param title   标题
     * @param message 消息内容
     * @param type    用户通知类型
     */
    public void addBroadcastSocketUserNotice(String title, String message, String type) {
        addBroadcastSocketMessage(UserNoticeMetadata.SYSTEM_USER_NOTICE_EVENT_NAME, UserNoticeMetadata.of(title, message, type));
    }


    /**
     * 添加多播用户通知
     *
     * @param room    房间 id
     * @param message 消息内容
     * @param type    用户通知类型
     */
    public void addBroadcastSocketUserNoticeByRoom(String room, String message, String type) {
        addBroadcastSocketMessage(room, UserNoticeMetadata.SYSTEM_USER_NOTICE_EVENT_NAME, UserNoticeMetadata.of(null, message, type));
    }

    /**
     * 添加多播用户通知
     *
     * @param room    房间 id
     * @param title   标题
     * @param message 消息内容
     * @param type    用户通知类型
     */
    public void addBroadcastSocketUserNoticeByRoom(String room, String title, String message, String type) {
        addBroadcastSocketMessage(room, UserNoticeMetadata.SYSTEM_USER_NOTICE_EVENT_NAME, UserNoticeMetadata.of(title, message, type));
    }

    /**
     * 添加 socket 结果集集合
     *
     * @param socketResults socket 结果集集合
     */
    public void addAllSocketResult(Collection<? extends SocketResult> socketResults) {

        if (CollectionUtils.isEmpty(socketResults)) {
            return;
        }

        socketResults.forEach(this::addSocketResult);
    }

    /**
     * 添加消息结果集
     *
     * @param socketResult 消息结果集
     */
    public void addSocketResult(SocketResult socketResult) {

        if (socketResult == null) {
            return;
        }

        if (CollectionUtils.isNotEmpty(socketResult.getBroadcastMessages())) {
            socketResult.getBroadcastMessages().forEach(this::addBroadcastSocketMessage);
        }

        if (CollectionUtils.isNotEmpty(socketResult.getMultipleUnicastMessages())) {
            socketResult.getMultipleUnicastMessages().forEach(this::addMultipleUnicastMessage);
        }

        if (CollectionUtils.isNotEmpty(socketResult.getUnicastMessages())) {
            socketResult.getUnicastMessages().forEach(this::addUnicastMessage);
        }
    }

    /**
     * 删除 existMessageList 集合理，存在 newMessage 事件类型的所有数据
     *
     * @param newMessage       新消息
     * @param existMessageList 已存在的消息集合
     */
    private void removeIfExist(AbstractSocketMessageMetadata<?> newMessage, List<? extends AbstractSocketMessageMetadata<?>> existMessageList) {

        if (!removeIfExist) {
            return;
        }

        List<AbstractSocketMessageMetadata<?>> exitList = existMessageList
                .stream()
                .filter(u -> u.getEvent().equals(newMessage.getEvent()))
                .collect(Collectors.toList());

        List<AbstractSocketMessageMetadata<?>> result = findSameSocketMessage(newMessage, new LinkedList<>(exitList));

        //noinspection SuspiciousMethodCalls
        existMessageList.removeAll(result);
    }


    /**
     * 查询相对事件的 socket 消息
     *
     * @param newMessage       新的数据
     * @param existMessageList 已存在的数据
     *
     * @return 相同事件的 socket 消息集合
     */
    private List<AbstractSocketMessageMetadata<?>> findSameSocketMessage(AbstractSocketMessageMetadata<?> newMessage, List<AbstractSocketMessageMetadata<?>> existMessageList) {

        Object newData = newMessage.getMessage().getData();

        List<AbstractSocketMessageMetadata<?>> result = new LinkedList<>();

        if (IdEntity.class.isAssignableFrom(newData.getClass())) {

            IdEntity<Object> newIdEntity = Casts.cast(newData);

            for (AbstractSocketMessageMetadata<?> message : existMessageList) {

                Object oldData = message.getMessage().getData();

                if (!IdEntity.class.isAssignableFrom(oldData.getClass())) {
                    continue;
                }

                IdEntity<Object> oldIdEntity = Casts.cast(oldData);

                if (Objects.equals(oldIdEntity.getId(), newIdEntity.getId())) {
                    result.add(message);
                }

            }

        }

        return result;
    }

    /**
     * 获取当前 socket result 下的所有值
     *
     * @return 值集合
     */
    public List<Object> getAllSocketValue() {

        List<Object> result = new LinkedList<>();

        if (CollectionUtils.isNotEmpty(getBroadcastMessages())) {
            getBroadcastMessages().forEach(b -> result.add(b.getMessage().getData()));
        }

        if (CollectionUtils.isNotEmpty(getMultipleUnicastMessages())) {
            getMultipleUnicastMessages().forEach(b -> result.add(b.getMessage().getData()));
        }

        if (CollectionUtils.isNotEmpty(getUnicastMessages())) {
            getUnicastMessages().forEach(b -> result.add(b.getMessage().getData()));
        }

        return result;
    }


    public boolean isRemoveIfExist() {
        return removeIfExist;
    }

    public void setRemoveIfExist(boolean removeIfExist) {
        this.removeIfExist = removeIfExist;
    }

    public List<UnicastMessageMetadata<?>> getUnicastMessages() {
        return unicastMessages;
    }

    public void setUnicastMessages(List<UnicastMessageMetadata<?>> unicastMessages) {
        this.unicastMessages = unicastMessages;
    }

    public List<MultipleUnicastMessageMetadata<?>> getMultipleUnicastMessages() {
        return multipleUnicastMessages;
    }

    public void setMultipleUnicastMessages(List<MultipleUnicastMessageMetadata<?>> multipleUnicastMessages) {
        this.multipleUnicastMessages = multipleUnicastMessages;
    }

    public List<BroadcastMessageMetadata<?>> getBroadcastMessages() {
        return broadcastMessages;
    }

    public void setBroadcastMessages(List<BroadcastMessageMetadata<?>> broadcastMessages) {
        this.broadcastMessages = broadcastMessages;
    }
}
