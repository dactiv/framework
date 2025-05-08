package com.github.dactiv.framework.fadada;

import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.config.PersonAuthConfig;
import com.github.dactiv.framework.fadada.config.SignTaskConfig;
import com.github.dactiv.framework.fadada.service.*;
import com.github.dactiv.framework.idempotent.advisor.concurrent.ConcurrentInterceptor;
import com.github.dactiv.framework.idempotent.config.IdempotentAutoConfiguration;
import com.github.dactiv.framework.spring.web.SpringWebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AutoConfigureAfter({SpringWebMvcAutoConfiguration.class, IdempotentAutoConfiguration.class})
@ConditionalOnProperty(prefix = "dactiv.fadada", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({FadadaConfig.class, PersonAuthConfig.class, SignTaskConfig.class})
public class FadadaAutoConfiguration {

    @Bean
    public AuthService fadadaAuthService(FadadaConfig fadadaConfig, ConcurrentInterceptor concurrentInterceptor, RestTemplate restTemplate) {
        return new AuthService(fadadaConfig, concurrentInterceptor, restTemplate);
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
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
