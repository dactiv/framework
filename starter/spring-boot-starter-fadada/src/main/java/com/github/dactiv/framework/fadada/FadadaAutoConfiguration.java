package com.github.dactiv.framework.fadada;

import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.config.PersonAuthConfig;
import com.github.dactiv.framework.fadada.config.SignTaskConfig;
import com.github.dactiv.framework.fadada.service.*;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnProperty(prefix = "dactiv.fadada.enabled", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({FadadaConfig.class, PersonAuthConfig.class, SignTaskConfig.class})
public class FadadaAutoConfiguration {

    @Bean
    public AuthService fadadaAuthService(FadadaConfig fadadaConfig, RedissonClient redissonClient, RestTemplate restTemplate) {
        return new AuthService(fadadaConfig, redissonClient, restTemplate);
    }

    @Bean
    public UserService fadadaUserService(FadadaConfig fadadaConfig, PersonAuthConfig personAuthConfig, AuthService authService, RestTemplate restTemplate) {
        return new UserService(fadadaConfig, personAuthConfig, authService, restTemplate);
    }

    @Bean
    public SignTaskService fadadaSignTaskService(FadadaConfig fadadaConfig, SignTaskConfig signTaskConfig, AuthService authService, RestTemplate restTemplate) {
        return new SignTaskService(fadadaConfig, signTaskConfig, authService, restTemplate);
    }

    @Bean
    public TemplateService fadadaTemplateService(FadadaConfig fadadaConfig, AuthService authService, RestTemplate restTemplate) {
        return new TemplateService(fadadaConfig, restTemplate, authService);
    }

    @Bean
    public DocService fadadaDocService(FadadaConfig fadadaConfig, AuthService authService, RestTemplate restTemplate) {
        return new DocService(fadadaConfig, restTemplate, authService);
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
