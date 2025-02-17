package com.github.dactiv.framework.security.audit.mongo;

import com.github.dactiv.framework.security.AuditConfiguration;
import com.github.dactiv.framework.security.AuditProperties;
import com.github.dactiv.framework.security.StoragePositionProperties;
import com.github.dactiv.framework.security.audit.AuditEventRepositoryInterceptor;
import com.github.dactiv.framework.security.audit.ExtendAuditEventRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.stream.Collectors;

/**
 * Mongo 审计仓库配置
 *
 * @author maurice.chen
 */
@ConditionalOnClass(MongoTemplate.class)
@ConditionalOnMissingBean(AuditEventRepository.class)
@EnableConfigurationProperties(AuditProperties.class)
@Conditional(AuditConfiguration.AuditImportSelectorCondition.class)
@ConditionalOnProperty(prefix = "dactiv.security.audit", value = "enabled", matchIfMissing = true)
public class MongoAuditConfiguration {

    @Bean
    public ExtendAuditEventRepository auditEventRepository(MongoTemplate mongoTemplate,
                                                           StoragePositionProperties storagePositionProperties,
                                                           ObjectProvider<AuditEventRepositoryInterceptor<Criteria>> interceptors) {

        return new MongoAuditEventRepository(
                interceptors.stream().collect(Collectors.toList()),
                mongoTemplate,
                storagePositionProperties
        );

    }
}