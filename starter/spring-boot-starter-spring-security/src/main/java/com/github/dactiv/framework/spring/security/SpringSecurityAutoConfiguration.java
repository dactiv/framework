package com.github.dactiv.framework.spring.security;

import com.github.dactiv.framework.spring.security.audit.ControllerAuditHandlerInterceptor;
import com.github.dactiv.framework.spring.security.audit.RequestBodyAttributeAdviceAdapter;
import com.github.dactiv.framework.spring.security.authentication.AccessTokenContextRepository;
import com.github.dactiv.framework.spring.security.authentication.UserDetailsService;
import com.github.dactiv.framework.spring.security.authentication.config.AccessTokenProperties;
import com.github.dactiv.framework.spring.security.authentication.config.OAuth2Properties;
import com.github.dactiv.framework.spring.security.authentication.config.RememberMeProperties;
import com.github.dactiv.framework.spring.security.authentication.config.SpringSecurityProperties;
import com.github.dactiv.framework.spring.security.authentication.handler.*;
import com.github.dactiv.framework.spring.security.authentication.provider.RequestAuthenticationProvider;
import com.github.dactiv.framework.spring.security.authentication.rememberme.CookieRememberService;
import com.github.dactiv.framework.spring.security.authentication.service.DefaultAuthenticationFailureResponse;
import com.github.dactiv.framework.spring.security.authentication.service.DefaultUserDetailsService;
import com.github.dactiv.framework.spring.security.authentication.service.feign.FeignAuthenticationTypeTokenResolver;
import com.github.dactiv.framework.spring.security.authentication.service.feign.FeignExceptionResultResolver;
import com.github.dactiv.framework.spring.security.controller.TokenController;
import com.github.dactiv.framework.spring.security.plugin.PluginEndpoint;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * spring security 重写支持自动配置类
 *
 * @author maurice
 */
@Configuration
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@ConditionalOnProperty(prefix = "dactiv.spring.security", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({SpringSecurityProperties.class, AccessTokenProperties.class, OAuth2Properties.class, RememberMeProperties.class})
public class SpringSecurityAutoConfiguration {

    @Bean
    @ConfigurationProperties("dactiv.spring.security.plugin")
    public PluginEndpoint pluginEndpoint(ObjectProvider<InfoContributor> infoContributor) {
        return new PluginEndpoint(infoContributor.stream().collect(Collectors.toList()));
    }

    @Bean
    @ConditionalOnProperty(prefix = "dactiv.spring.security.audit", name = "enabled", havingValue = "true")
    public ControllerAuditHandlerInterceptor controllerAuditHandlerInterceptor() {
        return new ControllerAuditHandlerInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(prefix = "dactiv.spring.security.access-token", value = "enable-controller", havingValue = "true")
    public TokenController accessTokenController(AccessTokenContextRepository accessTokenContextRepository,
                                          RedissonClient redissonClient) {
        return new TokenController(accessTokenContextRepository, redissonClient);
    }

    @Bean
    public DefaultUserDetailsService defaultUserDetailsService(PasswordEncoder passwordEncoder,
                                                        SpringSecurityProperties properties) {

        return new DefaultUserDetailsService(properties, passwordEncoder);
    }

    @Bean
    @ConditionalOnMissingBean(AccessTokenContextRepository.class)
    public AccessTokenContextRepository accessTokenContextRepository(SpringSecurityProperties springSecurityProperties,
                                                                     AccessTokenProperties accessTokenProperties,
                                                                     RedissonClient redissonClient) {

        return new AccessTokenContextRepository(
                redissonClient,
                springSecurityProperties,
                accessTokenProperties
        );
    }

    @Bean
    @ConditionalOnMissingBean(RememberMeServices.class)
    public CookieRememberService cookieRememberService(SpringSecurityProperties properties, RedissonClient redissonClient) {
        return new CookieRememberService(properties, redissonClient);
    }
    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler() {
        return new ForbiddenAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(JsonAuthenticationFailureHandler.class)
    public JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler(ObjectProvider<JsonAuthenticationFailureResponse> failureResponse,
                                                                             SpringSecurityProperties springSecurityProperties) {
        return new JsonAuthenticationFailureHandler(
                failureResponse.orderedStream().collect(Collectors.toList()),
                List.of(new AntPathRequestMatcher(springSecurityProperties.getLoginProcessingUrl(), HttpMethod.POST.name()))
        );
    }

    @Bean
    @ConditionalOnMissingBean(JsonAuthenticationSuccessHandler.class)
    public JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler(ObjectProvider<JsonAuthenticationSuccessResponse> successResponse,
                                                                             SpringSecurityProperties springSecurityProperties) {
        return new JsonAuthenticationSuccessHandler(
                successResponse.orderedStream().collect(Collectors.toList()),
                List.of(new AntPathRequestMatcher(springSecurityProperties.getLoginProcessingUrl(), HttpMethod.POST.name()))
        );
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authenticationManager(RedissonClient redissonClient,
                                                       SpringSecurityProperties springSecurityProperties,
                                                       ObjectProvider<UserDetailsService> userDetailsService) {
        return new RequestAuthenticationProvider(
                redissonClient,
                springSecurityProperties,
                userDetailsService.orderedStream().collect(Collectors.toList())
        );
    }

    @Bean
    public DefaultAuthenticationFailureResponse defaultAuthenticationFailureResponse(SpringSecurityProperties properties) {
        return new DefaultAuthenticationFailureResponse(properties);
    }

    @Bean
    public FeignExceptionResultResolver feignExceptionResultResolver() {
        if (isFeignExceptionClassAvailable()) {
            return new FeignExceptionResultResolver();
        }
        return null;
    }

    @Bean
    public FeignAuthenticationTypeTokenResolver feignAuthenticationTypeTokenResolver(SpringSecurityProperties properties) {
        if (isFeignExceptionClassAvailable()) {
            return new FeignAuthenticationTypeTokenResolver(properties);
        }
        return null;
    }

    private boolean isFeignExceptionClassAvailable() {
        try {
            Class.forName("feign.FeignException");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(prefix = "dactiv.spring.security.audit", name = "enabled", havingValue = "true")
    public static class DefaultWebMvcConfigurer implements WebMvcConfigurer {

        private final ControllerAuditHandlerInterceptor controllerAuditHandlerInterceptor;

        public DefaultWebMvcConfigurer(ControllerAuditHandlerInterceptor controllerAuditHandlerInterceptor) {
            this.controllerAuditHandlerInterceptor = controllerAuditHandlerInterceptor;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(controllerAuditHandlerInterceptor);
        }
    }

    @Bean
    @ConditionalOnMissingBean(RequestBodyAttributeAdviceAdapter.class)
    public RequestBodyAttributeAdviceAdapter requestBodyAttributeAdviceAdapter() {
        return new RequestBodyAttributeAdviceAdapter();
    }
}
