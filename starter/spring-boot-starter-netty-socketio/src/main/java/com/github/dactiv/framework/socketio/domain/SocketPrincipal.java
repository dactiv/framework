package com.github.dactiv.framework.socketio.domain;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.socketio.enumerate.ConnectStatus;
import com.github.dactiv.framework.spring.security.entity.support.MobileSecurityPrincipal;

import java.io.Serial;
import java.util.Date;
import java.util.Map;

/**
 * @author maurice.chen
 */
public class SocketPrincipal extends MobileSecurityPrincipal {

    @Serial
    private static final long serialVersionUID = -5635703490557755253L;
    /**
     * 创建时间
     */
    private Date creationTime = new Date();

    /**
     * 链接状态
     *
     * @see ConnectStatus
     */
    private ConnectStatus connectStatus;

    /**
     * 链接 socket 服务器的 ip 地址
     */
    /*@JsonIgnore
    private String socketServerIp;*/

    /**
     * 链接 socket 服务器的端口
     */
    /*@JsonIgnore
    private int port;*/

    /**
     * 链接成功时间
     */
    private Date connectionTime;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 元数据信息
     */
    private Map<String, Object> metadata;

    public SocketPrincipal() {
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public ConnectStatus getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(ConnectStatus connectStatus) {
        this.connectStatus = connectStatus;
    }

    /*public String getSocketServerIp() {
        return socketServerIp;
    }

    public void setSocketServerIp(String socketServerIp) {
        this.socketServerIp = socketServerIp;
    }*/

    /*public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }*/

    public Date getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(Date connectionTime) {
        this.connectionTime = connectionTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String getName() {
        return type + CacheProperties.DEFAULT_SEPARATOR + getId();
    }

    public String getFullName() {
        return type + CacheProperties.DEFAULT_SEPARATOR + getId() + CacheProperties.DEFAULT_SEPARATOR + getUsername();
    }
}
