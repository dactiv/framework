package com.github.dactiv.framework.security.test;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.security.StoragePositionProperties;
import com.github.dactiv.framework.security.audit.AuditEventRepositoryInterceptor;
import com.github.dactiv.framework.security.audit.elasticsearch.ElasticsearchAuditEventRepository;
import com.github.dactiv.framework.security.audit.mongo.MongoAuditEventRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
                                                                               ObjectProvider<AuditEventRepositoryInterceptor<BoolQuery.Builder>> interceptors) {

        return new ElasticsearchAuditEventRepository(
                interceptors.stream().collect(Collectors.toList()),
                elasticsearchOperations,
                storagePositionProperties
        );
    }

    @Bean
    @ConditionalOnProperty(name = "spring.profiles.active",havingValue = "mongo")
    public MongoAuditEventRepository mongoAuditEventRepository(MongoTemplate mongoTemplate,
                                                               StoragePositionProperties storagePositionProperties,
                                                               ObjectProvider<AuditEventRepositoryInterceptor<Criteria>> interceptors) {

        return new MongoAuditEventRepository(
                interceptors.stream().collect(Collectors.toList()),
                mongoTemplate,
                storagePositionProperties
        );

    }

    @Bean
    public ObjectMapper filterResultObjectMapper(Jackson2ObjectMapperBuilder builder){
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.registerModule(new JavaTimeModule());
        Casts.setObjectMapper(objectMapper);
        return objectMapper;
    }
}
