package com.github.dactiv.framework.spring.security;

import com.github.dactiv.framework.mybatis.plus.MybatisPlusAutoConfiguration;
import com.github.dactiv.framework.mybatis.plus.audit.MybatisPlusOperationDataTraceResolver;
import com.github.dactiv.framework.mybatis.plus.config.OperationDataTraceProperties;
import com.github.dactiv.framework.spring.security.audit.SecurityPrincipalOperationDataTraceResolver;
import com.github.dactiv.framework.spring.security.audit.config.ControllerAuditProperties;
import com.github.dactiv.framework.spring.security.authentication.config.SecurityPrincipalDataOwnerProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
@ConditionalOnMissingBean(MybatisPlusOperationDataTraceResolver.class)
@EnableConfigurationProperties(SecurityPrincipalDataOwnerProperties.class)
@ConditionalOnProperty(prefix = "dactiv.mybatis.operation-data-trace", value = "enabled", matchIfMissing = true)
public class SecurityOperationDataTraceRepositoryAutoConfiguration {

    @Bean
    public SecurityPrincipalOperationDataTraceResolver principalOperationDataTraceRepository(OperationDataTraceProperties operationDataTraceProperties,
                                                                                             ControllerAuditProperties controllerAuditProperties) {
        return new SecurityPrincipalOperationDataTraceResolver(operationDataTraceProperties, controllerAuditProperties);
    }

}

