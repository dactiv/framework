package com.github.dactiv.framework.spring.security.entity.support;

import com.github.dactiv.framework.security.entity.SecurityPrincipal;
import com.github.dactiv.framework.security.entity.support.SimpleSecurityPrincipal;
import com.github.dactiv.framework.security.enumerate.UserStatus;
import com.github.dactiv.framework.spring.security.entity.DeviceIdentifiedUserDetails;

import java.io.Serial;

/**
 * 移动端的用户明细实现
 *
 * @author maurice
 */
public class MobileSecurityPrincipal extends SimpleSecurityPrincipal implements DeviceIdentifiedUserDetails {

    @Serial
    private static final long serialVersionUID = -848955060608795664L;

    /**
     * 设备唯一识别
     */
    private String deviceIdentified;

    /**
     * 移动端用户明细实现
     */
    public MobileSecurityPrincipal() {}

    /**
     * 移动端用户明细实现
     *
     * @param securityPrincipal 安全用户接口
     * @param deviceIdentified 设备唯一是被
     */
    public MobileSecurityPrincipal(SecurityPrincipal securityPrincipal, String deviceIdentified) {
        this(securityPrincipal.getId(), securityPrincipal.getUsername(), securityPrincipal.getCredentials(), deviceIdentified);
    }

    /**
     * 移动端用户明细实现
     *
     * @param id               用户 id
     * @param username         登录账户
     * @param credentials         密码
     * @param deviceIdentified 设备唯一是被
     */
    public MobileSecurityPrincipal(Object id, String username, Object credentials, String deviceIdentified) {
        super(id, credentials, username, UserStatus.Enabled);
        this.deviceIdentified = deviceIdentified;
    }

    /**
     * 获取设备唯一识别
     *
     * @return 唯一识别
     */
    public String getDeviceIdentified() {
        return deviceIdentified;
    }

    /**
     * 设置设备唯一识别
     *
     * @param deviceIdentified 设备唯一识别
     */
    public void setDeviceIdentified(String deviceIdentified) {
        this.deviceIdentified = deviceIdentified;
    }

}
