package com.github.dactiv.framework.allin.pay;

import com.github.dactiv.framework.allin.pay.config.AllInPayProperties;
import com.github.dactiv.framework.allin.pay.service.AllInPayService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties(AllInPayProperties.class)
@ConditionalOnProperty(prefix = "dactiv.allin-pay", value = "enabled", matchIfMissing = true)
public class AllinPayAutoConfiguration {

    @Bean
    public AllInPayService allInPayService(AllInPayProperties properties, ObjectProvider<RestTemplate> restTemplate) {
        return new AllInPayService(properties, restTemplate.getIfAvailable(RestTemplate::new));
    }
}
