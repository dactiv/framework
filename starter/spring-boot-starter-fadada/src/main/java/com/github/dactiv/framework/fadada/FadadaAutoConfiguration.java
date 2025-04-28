package com.github.dactiv.framework.fadada;

import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.service.FadadaService;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(FadadaConfig.class)
public class FadadaAutoConfiguration {

    @Bean
    public FadadaService fadadaService(FadadaConfig fadadaConfig, RedissonClient redissonClient, RestTemplate restTemplate) {
        return new FadadaService(fadadaConfig, redissonClient, restTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
