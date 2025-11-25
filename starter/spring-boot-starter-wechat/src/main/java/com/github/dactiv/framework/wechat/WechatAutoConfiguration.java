package com.github.dactiv.framework.wechat;

import com.github.dactiv.framework.idempotent.advisor.concurrent.ConcurrentInterceptor;
import com.github.dactiv.framework.idempotent.config.IdempotentAutoConfiguration;
import com.github.dactiv.framework.wechat.service.WechatAppletService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(IdempotentAutoConfiguration.class)
@ConditionalOnProperty(prefix = "dactiv.wechat", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({AppletProperties.class, WechatProperties.class, OfficialProperties.class})
public class WechatAutoConfiguration {

    @Bean
    public WechatAppletService wechatAppletService(AppletProperties appletProperties,
                                                   WechatProperties wechatProperties,
                                                   ConcurrentInterceptor concurrentInterceptor) {

        return new WechatAppletService(appletProperties, wechatProperties,concurrentInterceptor);
    }

}
