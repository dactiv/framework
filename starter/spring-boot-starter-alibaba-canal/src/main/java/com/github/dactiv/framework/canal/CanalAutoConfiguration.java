package com.github.dactiv.framework.canal;

import com.github.dactiv.framework.canal.config.CanalAdminProperties;
import com.github.dactiv.framework.canal.config.CanalNoticeProperties;
import com.github.dactiv.framework.canal.config.CanalProperties;
import com.github.dactiv.framework.canal.endpoint.NotifiableTableEndpoint;
import com.github.dactiv.framework.canal.resolver.CanalRowDataChangeNoticeResolver;
import com.github.dactiv.framework.canal.resolver.CanalRowDataChangeResolver;
import com.github.dactiv.framework.canal.resolver.support.HttpCanalRowDataChangeNoticeResolver;
import com.github.dactiv.framework.canal.resolver.support.SimpleCanalRowDataChangeResolver;
import com.github.dactiv.framework.canal.service.CanalRowDataChangeNoticeService;
import com.github.dactiv.framework.canal.service.support.InMemoryCanalRowDataChangeNoticeService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.stream.Collectors;

/**
 * canal 自动配置类
 *
 * @author maurice.chen
 */
@Configuration
@EnableConfigurationProperties({CanalProperties.class, CanalAdminProperties.class, CanalNoticeProperties.class})
@ConditionalOnProperty(prefix = "dactiv.canal", value = "enabled", matchIfMissing = true)
public class CanalAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CanalInstanceManager.class)
    @ConditionalOnProperty(prefix = "dactiv.canal.admin", value = "enabled", matchIfMissing = true)
    public CanalAdminService canalAdminService(CanalAdminProperties canalAdminProperties,
                                               RestTemplate restTemplate,
                                               CanalInstanceManager canalInstanceManager,
                                               RedissonClient redissonClient) {
        return new CanalAdminService(
                canalAdminProperties,
                restTemplate,
                redissonClient,
                canalInstanceManager
        );
    }

    @Bean
    @ConditionalOnMissingBean(CanalInstanceManager.class)
    public CanalInstanceManager canalInstanceManager(ObjectProvider<CanalRowDataChangeResolver> canalRowDataChangeResolvers,
                                                     CanalProperties canalProperties) {
        return new CanalInstanceManager(
                canalRowDataChangeResolvers.stream().collect(Collectors.toList()),
                canalProperties
        );
    }

    @Bean
    @ConditionalOnMissingBean(HttpCanalRowDataChangeNoticeResolver.class)
    public HttpCanalRowDataChangeNoticeResolver httpCanalRowDataChangeNoticeResolver(ObjectProvider<RestTemplate> restTemplate) {
        return new HttpCanalRowDataChangeNoticeResolver(restTemplate.getIfAvailable(RestTemplate::new));
    }

    @Bean
    @ConditionalOnMissingBean(CanalRowDataChangeNoticeService.class)
    public CanalRowDataChangeNoticeService InMemoryCanalRowDataChangeNoticeService(ObjectProvider<CanalRowDataChangeNoticeResolver> canalRowDataChangeNoticeResolvers) {
        return new InMemoryCanalRowDataChangeNoticeService(canalRowDataChangeNoticeResolvers.stream().collect(Collectors.toList()));
    }

    @Bean
    public CanalRowDataChangeResolver simpleCanalRowDataChangeResolver(CanalRowDataChangeNoticeService canalRowDataChangeNoticeService) {
        return new SimpleCanalRowDataChangeResolver(canalRowDataChangeNoticeService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "dactiv.canal.notice", value = "enabled", matchIfMissing = true)
    public NotifiableTableEndpoint notifiableTableEndpoint(ObjectProvider<InfoContributor> infoContributors,
                                                           DataSource dataSource,
                                                           CanalNoticeProperties noticeProperties) {
        return new NotifiableTableEndpoint(infoContributors.stream().collect(Collectors.toList()), noticeProperties, dataSource);
    }
}
