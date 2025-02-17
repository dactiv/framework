package com.github.dactiv.framework.security.test;

import com.github.dactiv.framework.security.StoragePositionProperties;
import com.github.dactiv.framework.security.audit.AuditEventRepositoryInterceptor;
import com.github.dactiv.framework.security.audit.ExtendAuditEventRepository;
import com.github.dactiv.framework.security.audit.elasticsearch.ElasticsearchAuditEventRepository;
import com.github.dactiv.framework.security.audit.mongo.MongoAuditEventRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.stream.Collectors;

@EnableConfigurationProperties(StoragePositionProperties.class)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ConfigureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigureApplication.class, args);
    }

    @Bean
    public ElasticsearchAuditEventRepository elasticsearchAuditEventRepository(ElasticsearchOperations elasticsearchOperations,
                                                                               StoragePositionProperties storagePositionProperties,
                                                                               ObjectProvider<AuditEventRepositoryInterceptor> interceptors) {

        return new ElasticsearchAuditEventRepository(
                interceptors.stream().collect(Collectors.toList()),
                elasticsearchOperations,
                storagePositionProperties
        );
    }

    @Bean
    public ExtendAuditEventRepository auditEventRepository(MongoTemplate mongoTemplate,
                                                           StoragePositionProperties storagePositionProperties,
                                                           ObjectProvider<AuditEventRepositoryInterceptor> interceptors) {

        return new MongoAuditEventRepository(
                interceptors.stream().collect(Collectors.toList()),
                mongoTemplate,
                storagePositionProperties
        );

    }
}
