package com.github.dactiv.framework.spring.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.enumerate.NameEnum;
import com.github.dactiv.framework.commons.enumerate.NameValueEnum;
import com.github.dactiv.framework.commons.enumerate.ValueEnum;
import com.github.dactiv.framework.commons.jackson.deserializer.NameEnumDeserializer;
import com.github.dactiv.framework.commons.jackson.deserializer.NameValueEnumDeserializer;
import com.github.dactiv.framework.commons.jackson.deserializer.ValueEnumDeserializer;
import com.github.dactiv.framework.commons.jackson.serializer.NameEnumSerializer;
import com.github.dactiv.framework.commons.jackson.serializer.NameValueEnumSerializer;
import com.github.dactiv.framework.commons.jackson.serializer.ValueEnumSerializer;
import com.github.dactiv.framework.idempotent.exception.IdempotentException;
import com.github.dactiv.framework.spring.web.argument.DeviceHandlerMethodArgumentResolver;
import com.github.dactiv.framework.spring.web.argument.GenericsListHandlerMethodArgumentResolver;
import com.github.dactiv.framework.spring.web.device.DeviceResolverRequestFilter;
import com.github.dactiv.framework.spring.web.endpoint.EnumerateEndpoint;
import com.github.dactiv.framework.spring.web.interceptor.CustomClientHttpRequestInterceptor;
import com.github.dactiv.framework.spring.web.jackson.LocalDateTimeTimestampSerializer;
import com.github.dactiv.framework.spring.web.jackson.LocalDateTimestampSerializer;
import com.github.dactiv.framework.spring.web.jackson.LocalTimeSecondOfDaySerializer;
import com.github.dactiv.framework.spring.web.result.RestResponseBodyAdvice;
import com.github.dactiv.framework.spring.web.result.RestResultErrorAttributes;
import com.github.dactiv.framework.spring.web.result.error.ErrorResultResolver;
import com.github.dactiv.framework.spring.web.result.error.support.BindingResultErrorResultResolver;
import com.github.dactiv.framework.spring.web.result.error.support.ErrorCodeResultResolver;
import com.github.dactiv.framework.spring.web.result.error.support.IdempotentErrorResultResolver;
import com.github.dactiv.framework.spring.web.result.error.support.MissingServletRequestParameterResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.UndertowWebSocketServletWebServerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * spring mvc 自动配置实现
 *
 * @author maurice.chen
 */
@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties({SpringWebMvcProperties.class, JacksonProperties.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "dactiv.spring.web.mvc", value = "enabled", matchIfMissing = true)
public class SpringWebMvcAutoConfiguration {

    @Configuration
    @EnableConfigurationProperties(SpringWebMvcProperties.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class DefaultWebMvcConfigurer extends UndertowWebSocketServletWebServerCustomizer implements WebMvcConfigurer {

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(new GenericsListHandlerMethodArgumentResolver(getValidator()));
            argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
        }

    }

    @Bean
    @ConditionalOnMissingBean(RestResultErrorAttributes.class)
    public RestResultErrorAttributes servletRestResultErrorAttributes(List<ErrorResultResolver> resultResolvers,
                                                                      SpringWebMvcProperties properties) {
        return new RestResultErrorAttributes(
                resultResolvers,
                properties.getSupportException(),
                properties.getSupportHttpStatus()
        );
    }

    @Bean
    @ConditionalOnMissingBean(RestResponseBodyAdvice.class)
    public RestResponseBodyAdvice restResponseBodyAdvice(SpringWebMvcProperties properties) {
        return new RestResponseBodyAdvice(properties);
    }

    @Bean
    public FilterRegistrationBean<DeviceResolverRequestFilter> deviceResolverRequestFilter() {
        FilterRegistrationBean<DeviceResolverRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new DeviceResolverRequestFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    public BindingResultErrorResultResolver bindingResultErrorResultResolver() {
        return new BindingResultErrorResultResolver();
    }

    @Bean
    public MissingServletRequestParameterResolver missingServletRequestParameterResolver() {
        return new MissingServletRequestParameterResolver(MissingServletRequestParameterResolver.DEFAULT_OBJECT_NAME);
    }

    @Bean
    @ConditionalOnClass(IdempotentException.class)
    public IdempotentErrorResultResolver idempotentErrorResultResolver() {
        return new IdempotentErrorResultResolver();
    }

    @Bean
    public ErrorCodeResultResolver errorCodeResultResolver() {
        return new ErrorCodeResultResolver();
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public RestTemplate restTemplate(ObjectProvider<CustomClientHttpRequestInterceptor> clientHttpRequestInterceptors) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setInterceptors(clientHttpRequestInterceptors.stream().collect(Collectors.toList()));

        return restTemplate;
    }

    @Bean
    public ObjectMapper filterResultObjectMapper(Jackson2ObjectMapperBuilder builder, JacksonProperties jacksonProperties) {

        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        SimpleModule module = new SimpleModule();

        module.addSerializer(NameValueEnum.class, new NameValueEnumSerializer());
        module.addSerializer(ValueEnum.class, new ValueEnumSerializer());
        module.addSerializer(NameEnum.class, new NameEnumSerializer());

        module.addDeserializer(NameValueEnum.class, new NameValueEnumDeserializer<>());
        module.addDeserializer(ValueEnum.class, new ValueEnumDeserializer<>());
        module.addDeserializer(NameEnum.class, new NameEnumDeserializer<>());

        Map<SerializationFeature, Boolean> map = jacksonProperties.getSerialization();
        Boolean isWriteDatesAsTimestamps = map.get(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        if (Objects.nonNull(isWriteDatesAsTimestamps) && isWriteDatesAsTimestamps) {
            module.addSerializer(LocalDate.class, new LocalDateTimestampSerializer(jacksonProperties));
            module.addSerializer(LocalDateTime.class, new LocalDateTimeTimestampSerializer(jacksonProperties));
            module.addSerializer(LocalTime.class, new LocalTimeSecondOfDaySerializer());
        }

        objectMapper.registerModule(module);
        Casts.setObjectMapper(objectMapper);

        return objectMapper;
    }

    @Bean
    @ConfigurationProperties("dactiv.enumerate")
    public EnumerateEndpoint enumerateEndpoint(ObjectProvider<InfoContributor> infoContributor) {
        return new EnumerateEndpoint(infoContributor.stream().collect(Collectors.toList()));
    }

}
