package com.github.dactiv.framework.commons.domain.metadata;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.TimeProperties;

import java.io.Serial;
import java.util.concurrent.TimeUnit;

public class RefreshAccessTokenMetadata extends CloudSecretMetadata {

    @Serial
    private static final long serialVersionUID = 6636708642147562687L;

    /**
     * 刷新 token 提前时间配置
     */
    private TimeProperties refreshAccessTokenLeadTime = TimeProperties.of(10, TimeUnit.MINUTES);

    /**
     * 访问 token 缓存配置
     */
    private CacheProperties accessTokenCache;

    public RefreshAccessTokenMetadata() {
    }

    public RefreshAccessTokenMetadata(CacheProperties accessTokenCache) {
        this.accessTokenCache = accessTokenCache;
    }

    public RefreshAccessTokenMetadata(TimeProperties refreshAccessTokenLeadTime,
                                      CacheProperties accessTokenCache) {
        this.refreshAccessTokenLeadTime = refreshAccessTokenLeadTime;
        this.accessTokenCache = accessTokenCache;
    }

    public TimeProperties getRefreshAccessTokenLeadTime() {
        return refreshAccessTokenLeadTime;
    }

    public void setRefreshAccessTokenLeadTime(TimeProperties refreshAccessTokenLeadTime) {
        this.refreshAccessTokenLeadTime = refreshAccessTokenLeadTime;
    }

    public CacheProperties getAccessTokenCache() {
        return accessTokenCache;
    }

    public void setAccessTokenCache(CacheProperties accessTokenCache) {
        this.accessTokenCache = accessTokenCache;
    }
}
