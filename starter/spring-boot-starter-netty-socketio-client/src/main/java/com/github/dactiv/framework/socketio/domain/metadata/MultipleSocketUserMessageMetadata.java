package com.github.dactiv.framework.socketio.domain.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.socketio.domain.SocketPrincipal;
import com.github.dactiv.framework.spring.security.entity.support.MobileSecurityPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单数据循环单播 socket 消息实现, 该对象和 {@link MultipleUnicastMessageMetadata} 区别在于:本对象能直接知道 socket 服务器的具体 ip，可以直接发送。
 * 而 {@link MultipleUnicastMessageMetadata} 在 {@link com.github.dactiv.framework.socketio.SocketClientTemplate} 里根据所有的服务发现去循环发送，
 * 不管该设备识别在不在当前服务器。
 *
 * @author maurice
 */
public class MultipleSocketUserMessageMetadata<T> extends MultipleUnicastMessageMetadata<T> {

    @Serial
    private static final long serialVersionUID = 6233942402525531392L;

    /**
     * 客户端事件集合
     */
    @JsonIgnore
    private List<SocketPrincipal> socketPrincipals = new LinkedList<>();

    public MultipleSocketUserMessageMetadata() {
    }

    public MultipleSocketUserMessageMetadata(List<SocketPrincipal> socketPrincipals) {
        this.socketPrincipals = socketPrincipals;
    }

    public static <T> MultipleSocketUserMessageMetadata<T> ofSocketPrincipals(List<SocketPrincipal> socketPrincipals, String event, T data) {

        return ofSocketPrincipals(socketPrincipals, event, RestResult.ofSuccess(data));
    }

    public static <T> MultipleSocketUserMessageMetadata<T> ofSocketPrincipals(List<SocketPrincipal> socketPrincipals, String event, RestResult<T> message) {

        List<String> deviceIdentifiedList = socketPrincipals
                .stream()
                .map(MobileSecurityPrincipal::getDeviceIdentified)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());

        MultipleUnicastMessageMetadata<T> source = of(deviceIdentifiedList, event, message);
        MultipleSocketUserMessageMetadata<T> target = new MultipleSocketUserMessageMetadata<>(socketPrincipals);

        BeanUtils.copyProperties(source, target);

        target.setSocketPrincipals(socketPrincipals);

        return target;
    }

    public List<SocketPrincipal> getSocketPrincipals() {
        return socketPrincipals;
    }

    public void setSocketPrincipals(List<SocketPrincipal> socketPrincipals) {
        this.socketPrincipals = socketPrincipals;
    }
}
