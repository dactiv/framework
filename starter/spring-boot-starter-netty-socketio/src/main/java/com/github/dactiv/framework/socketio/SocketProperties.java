package com.github.dactiv.framework.socketio;

import com.corundumstudio.socketio.Configuration;
import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.TimeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * socket 配置
 *
 * @author maurice.chen
 */
@ConfigurationProperties("dactiv.socketio")
public class SocketProperties extends Configuration {


    /**
     * 用户缓存
     */
    private CacheProperties userCache = CacheProperties.of("dactiv:socketio:socket-user", TimeProperties.ofHours(2));

    /**
     * 请求返回结果时要忽略的字段内容
     */
    private Map<String, List<String>> ignoreResultMap = new LinkedHashMap<>();

    /**
     * 请求返回结果时要加 * 的数据内容映射
     */
    private Map<String, List<String>> desensitizeResultMap = new LinkedHashMap<>();

    public SocketProperties() {
    }

    public CacheProperties getUserCache() {
        return userCache;
    }

    public void setUserCache(CacheProperties userCache) {
        this.userCache = userCache;
    }

    public Map<String, List<String>> getIgnoreResultMap() {
        return ignoreResultMap;
    }

    public void setIgnoreResultMap(Map<String, List<String>> ignoreResultMap) {
        this.ignoreResultMap = ignoreResultMap;
    }

    public Map<String, List<String>> getDesensitizeResultMap() {
        return desensitizeResultMap;
    }

    public void setDesensitizeResultMap(Map<String, List<String>> desensitizeResultMap) {
        this.desensitizeResultMap = desensitizeResultMap;
    }
}
