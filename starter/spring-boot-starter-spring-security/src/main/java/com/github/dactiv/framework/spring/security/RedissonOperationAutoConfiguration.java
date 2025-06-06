package com.github.dactiv.framework.spring.security;

import com.github.dactiv.framework.spring.security.authentication.cache.CacheManager;
import com.github.dactiv.framework.spring.security.authentication.cache.support.RedissonCacheManager;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 缓存管理配置
 *
 * @author maurice.chen
 */
@Configuration
@AutoConfigureBefore({SpringSecurityAutoConfiguration.class})
@ConditionalOnClass({RedissonAutoConfigurationV2.class})
@ConditionalOnProperty(prefix = "dactiv.authentication.spring.security", value = "enabled", matchIfMissing = true)
public class RedissonOperationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager redissonCacheManager(RedissonClient redissonClient) {
        return new RedissonCacheManager(redissonClient);
    }

    /*@Bean
    @ConditionalOnClass(OAuth2AuthorizationService.class)
    @ConditionalOnMissingBean(OAuth2AuthorizationService.class)
    public OAuth2AuthorizationService redissonOAuth2AuthorizationService(RedissonClient redissonClient,
                                                                         OAuth2Properties oAuth2Properties) {
        return new RedissonOAuth2AuthorizationService(redissonClient, oAuth2Properties);
    }*/
}
