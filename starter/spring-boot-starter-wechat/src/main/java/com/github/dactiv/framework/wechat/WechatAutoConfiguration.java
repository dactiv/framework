package com.github.dactiv.framework.wechat;

import com.github.dactiv.framework.idempotent.advisor.concurrent.ConcurrentInterceptor;
import com.github.dactiv.framework.idempotent.config.IdempotentAutoConfiguration;
import com.github.dactiv.framework.wechat.config.AppletConfig;
import com.github.dactiv.framework.wechat.config.OfficialConfig;
import com.github.dactiv.framework.wechat.config.WechatConfig;
import com.github.dactiv.framework.wechat.service.WechatAppletService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AutoConfigureAfter(IdempotentAutoConfiguration.class)
@ConditionalOnProperty(prefix = "dactiv.wechat.enabled", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({AppletConfig.class, WechatConfig.class, OfficialConfig.class})
public class WechatAutoConfiguration {

    @Bean
    public WechatAppletService wechatAppletService(AppletConfig appletConfig,
                                                   WechatConfig wechatConfig,
                                                   RestTemplate restTemplate,
                                                   ConcurrentInterceptor concurrentInterceptor) {

        return new WechatAppletService(appletConfig,wechatConfig,restTemplate,concurrentInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
