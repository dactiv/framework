package com.github.dactiv.framework.socketio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.socketio.domain.SocketPrincipal;
import com.github.dactiv.framework.socketio.domain.SocketResult;
import com.github.dactiv.framework.socketio.domain.SocketUserMessage;
import com.github.dactiv.framework.socketio.domain.metadata.*;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.github.dactiv.framework.spring.security.authentication.service.feign.FeignAuthenticationConfiguration;
import com.github.dactiv.framework.spring.security.entity.support.MobileSecurityPrincipal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * socket 客户端模版，用于发送 socket 消息到客户端使用
 *
 * @author maurice.chen
 */
public class SocketClientTemplate implements DisposableBean {

    public static final Logger LOGGER = LoggerFactory.getLogger(SocketClientTemplate.class);

    private static final String DEFAULT_SERVER_SERVICE_ID = "authentication";

    public static final String JOIN_ROOM_BY_DEVICE_IDENTIFIES_TYPE = "joinRoomByDeviceIdentifies";

    public static final String LEAVE_ROOM_BY_DEVICE_IDENTIFIES_TYPE = "leaveRoomByDeviceIdentifies";

    public static final String JOIN_ROOM_BY_PRINCIPALS_TYPE = "joinRoomByPrincipals";

    public static final String LEAVE_ROOM_BY_PRINCIPALS_TYPE = "leaveRoomByPrincipals";

    public static final String DEVICE_IDENTIFIES_FIELD_NAME = "deviceIdentifies";

    public static final String PRINCIPALS_FIELD_NAME = "principals";

    public static final String ROOMS_FIELD_NAME = "rooms";

    private final DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    private final ThreadPoolTaskExecutor taskExecutor;

    private final AuthenticationProperties properties;

    public SocketClientTemplate(DiscoveryClient discoveryClient, RestTemplate restTemplate, ThreadPoolTaskExecutor taskExecutor, AuthenticationProperties properties) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
        this.taskExecutor = taskExecutor;
        this.properties = properties;
    }

    public static void asyncCommonLog(Object result, Throwable e) {

        if (Objects.nonNull(e)) {
            LOGGER.error("发送 socket 消息失败", e);
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("发送 socket 消息成功, 响应内容为:{}", result);
        }

    }

    /**
     * 构造加入/离开房间的 Http 实体
     *
     * @param object 唯一识别集合
     * @param rooms  房间集合
     * @return Http 实体
     */
    private HttpEntity<MultiValueMap<String, String>> createRoomHttpEntity(String objectFieldName,List<String> object,
                                                                           List<String> rooms) {
        HttpHeaders httpHeaders = FeignAuthenticationConfiguration.of(properties);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put(objectFieldName, object);
        data.put(ROOMS_FIELD_NAME, rooms);

        return new HttpEntity<>(data, httpHeaders);
    }

    /**
     * 加入房间
     *
     * @param deviceIdentifies 设备唯一是被
     * @param rooms            房间集合
     * @return 执行结果
     */
    public List<Map<String, Object>> joinRoomByDeviceIdentifies(List<String> deviceIdentifies, List<String> rooms) {
        HttpEntity<MultiValueMap<String, String>> entity = createRoomHttpEntity(DEVICE_IDENTIFIES_FIELD_NAME, deviceIdentifies, rooms);
        return exchangeDiscoveryOperation(entity, HttpMethod.POST, JOIN_ROOM_BY_DEVICE_IDENTIFIES_TYPE);
    }

    /**
     * 异步加入房间
     *
     * @param deviceIdentifies 设备唯一是被
     * @param rooms            房间集合
     * @return 执行结果
     */
    public CompletableFuture<List<Map<String, Object>>> asyncJoinRoomByDeviceIdentifies(List<String> deviceIdentifies, List<String> rooms)  {
        return taskExecutor.submitListenable(() -> joinRoomByDeviceIdentifies(deviceIdentifies, rooms)).completable();
    }

    /**
     * 离开房间
     *
     * @param deviceIdentifies 设备唯一是被
     * @param rooms            房间集合
     * @return 执行结果
     */
    public List<Map<String, Object>> leaveRoomByDeviceIdentifies(List<String> deviceIdentifies, List<String> rooms) {
        HttpEntity<MultiValueMap<String, String>> entity = createRoomHttpEntity(DEVICE_IDENTIFIES_FIELD_NAME, deviceIdentifies, rooms);
        return exchangeDiscoveryOperation(entity, HttpMethod.POST, LEAVE_ROOM_BY_DEVICE_IDENTIFIES_TYPE);
    }

    /**
     * 异步离开房间
     *
     * @param deviceIdentifies 设备唯一是被
     * @param rooms            房间集合
     * @return 执行结果
     */
    public CompletableFuture<List<Map<String, Object>>> asyncLeaveRoomByDeviceIdentifies(List<String> deviceIdentifies, List<String> rooms)  {
        return taskExecutor.submitListenable(() -> leaveRoomByDeviceIdentifies(deviceIdentifies, rooms)).completable();
    }

    /**
     * 加入房间
     *
     * @param principals 当前用户信息
     * @param rooms      房间集合
     * @return 执行结果
     */
    public List<Map<String, Object>> joinRoomByPrincipals(List<String> principals, List<String> rooms) {
        HttpEntity<MultiValueMap<String, String>> entity = createRoomHttpEntity(PRINCIPALS_FIELD_NAME, principals, rooms);
        return exchangeDiscoveryOperation(entity, HttpMethod.POST, JOIN_ROOM_BY_PRINCIPALS_TYPE);
    }

    /**
     * 异步加入房间
     *
     * @param principals 当前用户信息
     * @param rooms            房间集合
     * @return 执行结果
     */
    public CompletableFuture<List<Map<String, Object>>> asyncJoinRoomByPrincipals(List<String> principals, List<String> rooms)  {
        return taskExecutor.submitListenable(() -> joinRoomByPrincipals(principals, rooms)).completable();
    }

    /**
     * 离开房间
     *
     * @param principals 当前用户信息
     * @param rooms      房间集合
     * @return 执行结果
     */
    public List<Map<String, Object>> leaveRoomByPrincipals(List<String> principals, List<String> rooms) {
        HttpEntity<MultiValueMap<String, String>> entity = createRoomHttpEntity(PRINCIPALS_FIELD_NAME, principals, rooms);
        return exchangeDiscoveryOperation(entity, HttpMethod.POST, LEAVE_ROOM_BY_PRINCIPALS_TYPE);
    }

    /**
     * 异步离开房间
     *
     * @param principals 当前用户信息
     * @param rooms            房间集合
     * @return 执行结果
     */
    public CompletableFuture<List<Map<String, Object>>> asyncLeaveRoomByPrincipals(List<String> principals, List<String> rooms)  {
        return taskExecutor.submitListenable(() -> leaveRoomByPrincipals(principals, rooms)).completable();
    }

    /**
     * 执行房间操作
     *
     * @param details 用户明细集合
     * @param rooms   房间集合
     * @param type    类型:"leaveRoom" -> 离开房间, "joinRoom" -> 加入房间
     * @return 执行结果
     */
    public List<Map<String, Object>> exchangeRoomOperation(List<SocketPrincipal> details,
                                                           List<String> rooms,
                                                           String type) {
        Map<Optional<ServiceInstance>, List<SocketPrincipal>> groupDetails = details
                .stream()
                .collect(Collectors.groupingBy(this::getServiceInstanceOptional));

        List<Map<String, Object>> result = new LinkedList<>();

        for (Map.Entry<Optional<ServiceInstance>, List<SocketPrincipal>> entry : groupDetails.entrySet()) {

            Optional<ServiceInstance> optional = entry.getKey();

            if (optional.isEmpty()) {
                continue;
            }

            ServiceInstance serviceInstance = optional.get();

            List<String> deviceIdentifies = entry
                    .getValue()
                    .stream()
                    .map(MobileSecurityPrincipal::getDeviceIdentified)
                    .collect(Collectors.toList());

            HttpEntity<MultiValueMap<String, String>> entity = createRoomHttpEntity(DEVICE_IDENTIFIES_FIELD_NAME, deviceIdentifies, rooms);
            String url = this.createUrl(serviceInstance.getUri().toString(), type);
            Map<String, Object> data = exchangeOperation(url, entity, HttpMethod.POST);
            result.add(data);
        }

        return result;
    }

    /**
     * 获取服务实例
     *
     * @param details socket 用户明细
     * @return 服务实例的可选择对象
     */
    private Optional<ServiceInstance> getServiceInstanceOptional(SocketPrincipal details) {

        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(DEFAULT_SERVER_SERVICE_ID);

        return serviceInstances
                .stream()
                .filter(s -> s.getHost().equals(details.getSocketServerIp()))
                .findFirst();
    }

    /**
     * 广播消息
     *
     * @param broadcastMessage 多播 socket 消息对象
     * @return {@link RestResult} 的 map 映射
     */
    public Map<String, Object> broadcast(BroadcastMessageMetadata<?> broadcastMessage) {

        List<Map<String, Object>> result = broadcast(Collections.singletonList(broadcastMessage));

        if (result.isEmpty()) {
            return null;
        }

        return result.iterator().next();
    }

    /**
     * 广播消息
     *
     * @param broadcastMessages 多播 socket 消息对象集合
     * @return {@link RestResult} 的 map 映射集合
     */
    public List<Map<String, Object>> broadcast(List<BroadcastMessageMetadata<?>> broadcastMessages) {
        return postSocketMessage(BroadcastMessageMetadata.DEFAULT_TYPE, broadcastMessages);
    }

    /**
     * 异步广播消息
     *
     * @param broadcastMessage 多播 socket 消息对象集合
     */
    public CompletableFuture<Map<String, Object>> asyncBroadcast(BroadcastMessageMetadata<?> broadcastMessage) {
        return taskExecutor.submitListenable(() -> broadcast(broadcastMessage)).completable();
    }

    /**
     * 异步广播消息
     *
     * @param broadcastMessages 多播 socket 消息对象集合
     */
    public CompletableFuture<List<Map<String, Object>>> asyncBroadcast(List<BroadcastMessageMetadata<?>> broadcastMessages) {
        return taskExecutor
                .submitListenable(() -> broadcast(broadcastMessages))
                .completable();
    }

    /**
     * 单播信息
     *
     * @param unicastMessage 单播 socket 消息对象
     * @return {@link RestResult} 的 map 映射
     */
    public Map<String, Object> unicast(UnicastMessageMetadata<?> unicastMessage) {

        List<Map<String, Object>> result = unicast(Collections.singletonList(unicastMessage));

        if (result.isEmpty()) {
            return null;
        }

        return result.iterator().next();
    }

    /**
     * 单播信息
     *
     * @param unicastMessages 单播 socket 消息对象集合
     * @return {@link RestResult} 的 map 映射集合
     */
    public List<Map<String, Object>> unicast(List<UnicastMessageMetadata<?>> unicastMessages) {

        List<UnicastMessageMetadata<?>> tempList = new LinkedList<>(unicastMessages);

        List<SocketUserMessage<?>> socketUserMessages = tempList
                .stream()
                .filter(u -> SocketUserMessage.class.isAssignableFrom(u.getClass()))
                .map(Casts::<SocketUserMessage<?>>cast)
                .collect(Collectors.toList());

        tempList.removeAll(socketUserMessages);

        List<Map<String, Object>> result = socketUserMessages
                .stream()
                .collect(Collectors.groupingBy(s -> this.getServiceInstanceOptional(s.getPrincipal())))
                .entrySet()
                .stream()
                .filter(e -> e.getKey().isPresent())
                .flatMap(e -> postSocketMessage(e.getKey().get().getHost(), SocketUserMessage.DEFAULT_TYPE, e.getValue()).stream())
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(tempList)) {
            List<Map<String, Object>> tempResult = postSocketMessage(UnicastMessageMetadata.DEFAULT_TYPE, tempList);

            result.addAll(tempResult);
        }

        return result;

    }

    /**
     * 异步单播信息
     *
     * @param unicastMessage 多播 socket 消息对象
     */
    public CompletableFuture<Map<String, Object>> asyncUnicast(UnicastMessageMetadata<?> unicastMessage) {
        return asyncUnicast(unicastMessage, null);
    }

    /**
     * 异步单播信息
     *
     * @param unicastMessage  多播 socket 消息对象集合
     * @param filterResultIds 过滤结果集 id 集合
     */
    public CompletableFuture<Map<String, Object>> asyncUnicast(UnicastMessageMetadata<?> unicastMessage,
                                                               List<String> filterResultIds) {
        return taskExecutor
                .submitListenable(() -> unicast(unicastMessage))
                .completable();
    }

    /**
     * 异步单播信息
     *
     * @param unicastMessages 单播 socket 消息对象集合
     */
    public CompletableFuture<List<Map<String, Object>>> asyncUnicast(List<UnicastMessageMetadata<?>> unicastMessages) {
        return taskExecutor
                .submitListenable(() -> unicast(unicastMessages))
                .completable();
    }

    /**
     * 单播单数据循环单播 socket 消息
     *
     * @param multipleUnicastMessage 单数据循环单播 socket 消息
     * @return {@link RestResult} 的 map 映射
     */
    public Map<String, Object> multipleUnicast(MultipleUnicastMessageMetadata<?> multipleUnicastMessage) {

        List<Map<String, Object>> result = multipleUnicast(Collections.singletonList(multipleUnicastMessage));

        if (result.isEmpty()) {
            return null;
        }

        return result.iterator().next();
    }

    /**
     * 单播单数据循环单播 socket 消息
     *
     * @param multipleUnicastMessages 单数据循环单播 socket 消息集合
     * @return {@link RestResult} 的 map 映射集合
     */
    public List<Map<String, Object>> multipleUnicast(List<MultipleUnicastMessageMetadata<?>> multipleUnicastMessages) {

        List<MultipleUnicastMessageMetadata<?>> tempList = new LinkedList<>(multipleUnicastMessages);

        List<MultipleSocketUserMessageMetadata<?>> socketUserMessages = tempList
                .stream()
                .filter(u -> MultipleSocketUserMessageMetadata.class.isAssignableFrom(u.getClass()))
                .map(Casts::<MultipleSocketUserMessageMetadata<?>>cast)
                .collect(Collectors.toList());

        tempList.removeAll(socketUserMessages);

        Map<String, List<MultipleSocketUserMessageMetadata<?>>> postData = new LinkedHashMap<>();

        for (MultipleSocketUserMessageMetadata<?> message : socketUserMessages) {

            Map<Optional<ServiceInstance>, List<SocketPrincipal>> group = message
                    .getSocketPrincipals()
                    .stream()
                    .collect(Collectors.groupingBy(this::getServiceInstanceOptional));

            for (Map.Entry<Optional<ServiceInstance>, List<SocketPrincipal>> entry : group.entrySet()) {
                Optional<ServiceInstance> optional = entry.getKey();

                if (optional.isEmpty()) {
                    continue;
                }

                List<MultipleSocketUserMessageMetadata<?>> list = postData.computeIfAbsent(optional.get().getHost(), k -> new LinkedList<>());

                MultipleSocketUserMessageMetadata<?> data = MultipleSocketUserMessageMetadata.ofSocketPrincipals(
                        entry.getValue(),
                        message.getEvent(),
                        message.getMessage()
                );

                list.add(data);

            }

        }

        List<Map<String, Object>> result = postData
                .entrySet()
                .stream()
                .flatMap(e -> postSocketMessage(e.getKey(), MultipleUnicastMessageMetadata.DEFAULT_TYPE, e.getValue()).stream())
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(tempList)) {
            List<Map<String, Object>> tempResult = postSocketMessage(UnicastMessageMetadata.DEFAULT_TYPE, tempList);

            result.addAll(tempResult);
        }

        return result;
    }

    /**
     * 异步单数据循环单播
     *
     * @param multipleUnicastMessage 单数据循环单播 socket 消息
     */
    public CompletableFuture<Map<String, Object>> asyncMultipleUnicast(MultipleUnicastMessageMetadata<?> multipleUnicastMessage) {
        return taskExecutor
                .submitListenable(() -> multipleUnicast(multipleUnicastMessage))
                .completable();
    }

    /**
     * 异步单数据循环单播
     *
     * @param multipleUnicastMessages 单播 socket 消息对象集合
     */
    public CompletableFuture<List<Map<String, Object>>> asyncMultipleUnicast(List<MultipleUnicastMessageMetadata<?>> multipleUnicastMessages) {
        return taskExecutor
                .submitListenable(() -> multipleUnicast(multipleUnicastMessages))
                .completable();
    }

    /**
     * 提交 socket 消息到指定 socket 服务器
     *
     * @param type   消息类型: multipleUnicast 单数据循环单播，unicast: 根据设备识别单播，broadcast: 广播
     * @param values socket 消息集合
     * @return {@link RestResult} 的 map 映射集合
     */
    private List<Map<String, Object>> postSocketMessage(String type, List<? extends AbstractSocketMessageMetadata<?>> values) {
        return postSocketMessage(null, type, values);
    }

    /**
     * 提交 socket 消息到指定 socket 服务器
     *
     * @param host   socket 服务 ip 地址
     * @param type   消息类型: unicastUser: 根据用户所在服务器单播，
     *               unicast: 根据设备识别单播，
     *               broadcast: 广播,
     *               multipleUnicast: 同数据结果，单播多客户端
     * @param values socket 消息集合
     * @return {@link RestResult} 的 map 映射集合
     */
    private List<Map<String, Object>> postSocketMessage(String host, String type, List<? extends AbstractSocketMessageMetadata<?>> values) {

        HttpHeaders httpHeaders = FeignAuthenticationConfiguration.of(properties);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<Map<String, Object>> data = Casts.convertValue(values, new TypeReference<>() {
        });

        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(data, httpHeaders);
        List<Map<String, Object>> result = new LinkedList<>();

        if (StringUtils.isEmpty(host)) {
            List<List<Map<String, Object>>> restData = exchangeDiscoveryOperation(entity, HttpMethod.POST, type);
            restData.forEach(result::addAll);
        } else {

            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(DEFAULT_SERVER_SERVICE_ID);

            ServiceInstance instance = serviceInstances
                    .stream()
                    .filter(s -> s.getHost().equals(host))
                    .findFirst()
                    .orElseThrow(() -> new SystemException("找不到 IP 为 [" + host + "] 的服务实例"));

            String url = this.createUrl(instance.getUri().toString(), type);

            List<Map<String, Object>> response = exchangeOperation(url, entity, HttpMethod.POST);
            if (Objects.nonNull(response)) {
                result.addAll(response);
            }
        }

        return result;
    }

    /**
     * 执行服务发现操作
     *
     * @param entity http 实体
     * @param method 提交方式
     * @param name   执行后缀
     * @return 每个服务执行结果集合
     */
    private <T> List<T> exchangeDiscoveryOperation(HttpEntity<?> entity, HttpMethod method, String name) {
        List<T> result = new LinkedList<>();

        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(DEFAULT_SERVER_SERVICE_ID);

        List<String> urls = serviceInstances
                .stream()
                .map(s -> this.createUrl(s.getUri().toString(), name))
                .toList();

        for (String url : urls) {

            T data = exchangeOperation(url, entity, method);

            if (Objects.nonNull(data)) {
                result.add(data);
            }
        }

        return result;
    }

    private <T> T exchangeOperation(String url, HttpEntity<?> entity, HttpMethod method) {
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                method,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

    /**
     * 根据服务实例和接口名称创建 url
     *
     * @param uri  服务地址 uri
     * @param name 接口名称
     * @return 完整 url 路径
     */
    private String createUrl(String uri, String name) {
        String prefix = StringUtils.appendIfMissing(uri, AntPathMatcher.DEFAULT_PATH_SEPARATOR);
        return prefix + StringUtils.removeStart(name, AntPathMatcher.DEFAULT_PATH_SEPARATOR);
    }

    /**
     * 发送 socket 结果集
     *
     * @param result socket 结果集
     */
    public void sendSocketResult(SocketResult result) {

        if (CollectionUtils.isNotEmpty(result.getUnicastMessages())) {
            unicast(result.getUnicastMessages());
        }

        if (CollectionUtils.isNotEmpty(result.getBroadcastMessages())) {
            broadcast(result.getBroadcastMessages());
        }

        if (CollectionUtils.isNotEmpty(result.getMultipleUnicastMessages())) {
            multipleUnicast(result.getMultipleUnicastMessages());
        }
    }

    /**
     * 异步发送 socket 结果集
     *
     * @param result socket 结果集
     */
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

    @Override
    public void destroy() {
        taskExecutor.destroy();
    }
}
