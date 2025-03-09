package com.github.dactiv.framework.minio.config;

import com.github.dactiv.framework.commons.TimeProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;

import java.util.concurrent.TimeUnit;

/**
 * minio 配置信息
 *
 * @author maurice.chen
 */
@ConfigurationProperties("dactiv.minio")
public class MinioProperties {

    /**
     * 终端地址
     */
    private String endpoint;

    /**
     * 管理端 api 地址
     */
    private String consoleEndpoint;

    /**
     * 管理端 api 前缀
     */
    private String consoleApiPrefix = "/api/v1";

    /**
     * 管理端登录请求体 accessKey 名称
     */
    private String accessKeyBodyName = "accessKey";

    /**
     * 管理端登录请求体 secretKey 名称
     */
    private String secretKeyBodyName = "secretKey";

    /**
     * 大文件上传的临时文件存储位置
     */
    private String uploadPartTempFilePath = "./upload_part";

    /**
     * 用于隔离大文件上传，批量删除等多现成操作的专有线程池储量，如果为 null 默认取值为: Runtime.getRuntime().availableProcessors() * 2
     */
    private Integer ioExecutorNumberOfThreads = null;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 安全密钥
     */
    private String secretKey;

    /**
     * admin 后台 cookie 剩余最小时间后进行刷新，默认为 5 分钟
     */
    private TimeProperties cookieMinRemainingBeforeRefresh = TimeProperties.of(5, TimeUnit.MINUTES);

    /**
     * 刷新 cookie 时间
     */
    private String refreshCookieCron = "0 1 * * * ?";

    /**
     * minio 配置信息
     */
    public MinioProperties() {
    }

    /**
     * 获取终端地址
     *
     * @return 终端地址
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * 设置终端地址
     *
     * @param endpoint 终端地址
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * 获取访问密钥
     *
     * @return 访问密钥
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * 设置访问密钥
     *
     * @param accessKey 访问密钥
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * 获取安全密钥
     *
     * @return 安全密钥
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 设置安全密钥
     *
     * @param secretKey 安全密钥
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAccessKeyBodyName() {
        return accessKeyBodyName;
    }

    public void setAccessKeyBodyName(String accessKeyBodyName) {
        this.accessKeyBodyName = accessKeyBodyName;
    }

    public String getSecretKeyBodyName() {
        return secretKeyBodyName;
    }

    public void setSecretKeyBodyName(String secretKeyBodyName) {
        this.secretKeyBodyName = secretKeyBodyName;
    }

    /**
     * 设置控制台终端地址
     *
     * @return 控制台终端地址
     */
    public String getConsoleEndpoint() {
        return consoleEndpoint;
    }

    /**
     * 设置控制台终端地址
     * @param consoleEndpoint 控制台终端地址
     */
    public void setConsoleEndpoint(String consoleEndpoint) {
        this.consoleEndpoint = consoleEndpoint;
    }

    /**
     * 获取管理端 api 前缀
     *
     * @return 管理端 api 前缀
     */
    public String getConsoleApiPrefix() {
        return consoleApiPrefix;
    }

    /**
     * 设置管理端 api 前缀
     *
     * @param consoleApiPrefix 管理端 api 前缀
     */
    public void setConsoleApiPrefix(String consoleApiPrefix) {
        this.consoleApiPrefix = consoleApiPrefix;
    }

    public String getConsoleApiAddress(String apiName) {
        return getConsoleEndpoint()
                + StringUtils.prependIfMissing(getConsoleApiPrefix(), AntPathMatcher.DEFAULT_PATH_SEPARATOR)
                + StringUtils.prependIfMissing(apiName, AntPathMatcher.DEFAULT_PATH_SEPARATOR);
    }

    public TimeProperties getCookieMinRemainingBeforeRefresh() {
        return cookieMinRemainingBeforeRefresh;
    }

    public void setCookieMinRemainingBeforeRefresh(TimeProperties cookieMinRemainingBeforeRefresh) {
        this.cookieMinRemainingBeforeRefresh = cookieMinRemainingBeforeRefresh;
    }

    public String getRefreshCookieCron() {
        return refreshCookieCron;
    }

    public void setRefreshCookieCron(String refreshCookieCron) {
        this.refreshCookieCron = refreshCookieCron;
    }

    public String getUploadPartTempFilePath() {
        return uploadPartTempFilePath;
    }

    public void setUploadPartTempFilePath(String uploadPartTempFilePath) {
        this.uploadPartTempFilePath = uploadPartTempFilePath;
    }

    public Integer getIoExecutorNumberOfThreads() {
        return ioExecutorNumberOfThreads;
    }

    public void setIoExecutorNumberOfThreads(Integer ioExecutorNumberOfThreads) {
        this.ioExecutorNumberOfThreads = ioExecutorNumberOfThreads;
    }
}
