package com.github.dactiv.framework.citic;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.github.dactiv.framework.citic.config.CiticProperties;
import com.github.dactiv.framework.citic.service.CiticService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@ConditionalOnProperty(prefix = "dactiv.citic", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(CiticProperties.class)
public class CiticAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter(Jackson2ObjectMapperBuilder builder) {
        XmlMapper xmlMapper = builder.createXmlMapper(true).build();
        xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
        return new MappingJackson2XmlHttpMessageConverter(xmlMapper);
    }

    @Bean
    public CiticService citicService(MappingJackson2XmlHttpMessageConverter converter,
                                     ObjectProvider<RestTemplate> objectProvider,
                                     CiticProperties citicProperties) {
        return new CiticService(objectProvider.getIfAvailable(RestTemplate::new), citicProperties, converter);
    }

}
