package com.github.dactiv.framework.socketio.domain.metadata;

import com.github.dactiv.framework.commons.RestResult;

import java.io.Serial;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单数据循环单播 socket 消息实现， 用于在存在一个数据内容，发送给多个用户的时候使用
 *
 * @param <T>
 *
 * @author maurice
 */
public class MultipleUnicastMessageMetadata<T> extends AbstractSocketMessageMetadata<T> {

    @Serial
    private static final long serialVersionUID = 6233942402525531392L;

    public static final String DEFAULT_TYPE = "multipleUnicast";

    /**
     * 客户端事件集合
     */
    private List<String> deviceIdentifiedList = new LinkedList<>();

    public MultipleUnicastMessageMetadata() {
    }

    public MultipleUnicastMessageMetadata(List<String> deviceIdentifiedList) {
        this.deviceIdentifiedList = deviceIdentifiedList;
    }

    /**
     * 转换为单播 socket 消息
     *
     * @return 单播 socket 消息集合
     */
    public List<UnicastMessageMetadata<?>> toUnicastMessageList() {
        return deviceIdentifiedList
                .stream()
                .map(c -> UnicastMessageMetadata.of(c, getEvent(), getMessage()))
                .collect(Collectors.toList());
    }

    public static <T> MultipleUnicastMessageMetadata<T> of(List<String> deviceIdentifiedList, String event, T data) {

        return of(deviceIdentifiedList, event, RestResult.ofSuccess(data));
    }

    public static <T> MultipleUnicastMessageMetadata<T> of(List<String> deviceIdentifiedList, String event, RestResult<T> message) {
        MultipleUnicastMessageMetadata<T> result = new MultipleUnicastMessageMetadata<>(deviceIdentifiedList);
        result.setEvent(event);
        result.setMessage(message);
        return result;
    }

    @Override
    public String getType() {
        return DEFAULT_TYPE;
    }

    public List<String> getDeviceIdentifiedList() {
        return deviceIdentifiedList;
    }

    public void setDeviceIdentifiedList(List<String> deviceIdentifiedList) {
        this.deviceIdentifiedList = deviceIdentifiedList;
    }
}
