package com.github.dactiv.framework.spring.security;

import com.github.dactiv.framework.security.entity.RoleAuthority;
import com.github.dactiv.framework.spring.security.audit.ControllerAuditHandlerInterceptor;
import com.github.dactiv.framework.spring.security.audit.RequestBodyAttributeAdviceAdapter;
import com.github.dactiv.framework.spring.security.audit.SecurityAuditEventRepositoryInterceptor;
import com.github.dactiv.framework.spring.security.audit.config.ControllerAuditProperties;
import com.github.dactiv.framework.spring.security.authentication.AccessTokenContextRepository;
import com.github.dactiv.framework.spring.security.authentication.TypeSecurityPrincipalService;
import com.github.dactiv.framework.spring.security.authentication.cache.CacheManager;
import com.github.dactiv.framework.spring.security.authentication.cache.support.InMemoryCacheManager;
import com.github.dactiv.framework.spring.security.authentication.config.*;
import com.github.dactiv.framework.spring.security.authentication.handler.*;
import com.github.dactiv.framework.spring.security.authentication.provider.TypeRememberMeAuthenticationProvider;
import com.github.dactiv.framework.spring.security.authentication.service.TypeSecurityPrincipalManager;
import com.github.dactiv.framework.spring.security.authentication.service.TypeTokenBasedRememberMeServices;
import com.github.dactiv.framework.spring.security.controller.TokenController;
import com.github.dactiv.framework.spring.security.plugin.PluginEndpoint;
import org.apache.commons.lang3.StringUtils;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.websocket.servlet.UndertowWebSocketServletWebServerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * spring security 重写支持自动配置类
 *
 * @author maurice
 */
@Configuration
@AutoConfigureAfter(RedissonAutoConfigurationV2.class)
@EnableConfigurationProperties({
        AuthenticationProperties.class,
        AccessTokenProperties.class,
        CaptchaVerificationProperties.class,
        ControllerAuditProperties.class,
        OAuth2Properties.class
})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "dactiv.authentication.spring.security", value = "enabled", matchIfMissing = true)
public class SpringSecurityAutoConfiguration {

    @Bean
    @ConfigurationProperties("dactiv.authentication.plugin")
    public PluginEndpoint pluginEndpoint(ObjectProvider<InfoContributor> infoContributor) {
        return new PluginEndpoint(infoContributor.stream().collect(Collectors.toList()));
    }

    @Bean
    @ConditionalOnProperty(prefix = "dactiv.security.audit", name = "enabled", havingValue = "true")
    public ControllerAuditHandlerInterceptor controllerAuditHandlerInterceptor(ControllerAuditProperties controllerAuditProperties) {
        return new ControllerAuditHandlerInterceptor(controllerAuditProperties);
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(prefix = "dactiv.authentication.access-token", value = "enable-controller", havingValue = "true")
    public TokenController accessTokenController(CacheManager cacheManager,
                                                 AccessTokenProperties accessTokenProperties) {
        return new TokenController(cacheManager, accessTokenProperties);
    }

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager inMemoryCacheManager() {
        return new InMemoryCacheManager();
    }

    @Bean
    @ConditionalOnMissingBean(AccessTokenContextRepository.class)
    public AccessTokenContextRepository accessTokenContextRepository(AuthenticationProperties properties,
                                                                     AccessTokenProperties accessTokenProperties,
                                                                     CacheManager cacheManager) {

        return new AccessTokenContextRepository(
                cacheManager,
                accessTokenProperties,
                properties
        );
    }

    @Bean
    @ConditionalOnMissingBean(JsonAuthenticationFailureHandler.class)
    public JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler(ObjectProvider<JsonAuthenticationFailureResponse> failureResponse,
                                                                             AuthenticationProperties authenticationProperties) {
        return new JsonAuthenticationFailureHandler(
                failureResponse.orderedStream().collect(Collectors.toList()),
                authenticationProperties
        );
    }

    @Bean
    @ConditionalOnMissingBean(JsonAuthenticationSuccessHandler.class)
    public JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler(ObjectProvider<JsonAuthenticationSuccessResponse> successResponse,
                                                                             AuthenticationProperties properties,
                                                                             OAuth2Properties oAuth2Properties) {

        List<AntPathRequestMatcher> antPathRequestMatchers = new LinkedList<>();

        antPathRequestMatchers.add(new AntPathRequestMatcher(oAuth2Properties.getAuthorizeEndpoint()));
        antPathRequestMatchers.add(new AntPathRequestMatcher(oAuth2Properties.getTokenEndpoint()));
        antPathRequestMatchers.add(new AntPathRequestMatcher(oAuth2Properties.getOidcUserInfoEndpoint()));

        return new JsonAuthenticationSuccessHandler(
                successResponse.orderedStream().collect(Collectors.toList()),
                properties,
                antPathRequestMatchers
        );
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(prefix = "dactiv.security.audit", name = "enabled", havingValue = "true")
    public static class DefaultWebMvcConfigurer extends UndertowWebSocketServletWebServerCustomizer implements WebMvcConfigurer {

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

    @Bean
    @ConditionalOnMissingBean(SecurityAuditEventRepositoryInterceptor.class)
    public SecurityAuditEventRepositoryInterceptor securityAuditEventRepositoryInterceptor(AuthenticationProperties authenticationProperties,
                                                                                           RememberMeProperties rememberMeProperties) {
        return new SecurityAuditEventRepositoryInterceptor(authenticationProperties, rememberMeProperties);
    }

    @Bean
    @ConditionalOnMissingBean(TypeSecurityPrincipalManager.class)
    public TypeSecurityPrincipalManager typeSecurityPrincipalManager(CacheManager cacheManager,
                                                                     ObjectProvider<TypeSecurityPrincipalService> typeSecurityPrincipalServices) {

        return new TypeSecurityPrincipalManager(
                typeSecurityPrincipalServices.stream().collect(Collectors.toList()),
                cacheManager
        );

    }

    @Bean
    @ConditionalOnMissingBean(TypeRememberMeAuthenticationProvider.class)
    public TypeRememberMeAuthenticationProvider typeRememberMeAuthenticationProvider(RememberMeProperties rememberMe,
                                                                                     TypeSecurityPrincipalManager typeSecurityPrincipalManager) {
        return new TypeRememberMeAuthenticationProvider(rememberMe.getKey(), typeSecurityPrincipalManager);
    }

    @Bean
    @ConditionalOnMissingBean(TypeTokenBasedRememberMeServices.class)
    public TypeTokenBasedRememberMeServices typeTokenBasedRememberMeServices(TypeSecurityPrincipalManager typeSecurityPrincipalManager,
                                                                             RememberMeProperties rememberMeProperties,
                                                                             UserDetailsManager userDetailsManager) {
        return new TypeTokenBasedRememberMeServices(rememberMeProperties, typeSecurityPrincipalManager, userDetailsManager);
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(AuthenticationProperties authenticationProperties,
                                                         PasswordEncoder passwordEncoder) {

        List<UserDetails> userDetails = new LinkedList<>();
        for (SecurityProperties.User user : authenticationProperties.getUsers()) {
            List<SimpleGrantedAuthority> roleAuthorities = user
                    .getRoles()
                    .stream()
                    .map(s -> StringUtils.prependIfMissing(s, RoleAuthority.DEFAULT_ROLE_PREFIX))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            userDetails.add(new User(user.getName(), passwordEncoder.encode(user.getPassword()), roleAuthorities));
        }
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    @ConditionalOnMissingBean(IgnoreAuthenticationSuccessDataResponse.class)
    public IgnoreAuthenticationSuccessDataResponse ignoreAuthenticationSuccessDataResponse(AuthenticationProperties properties) {
        return new IgnoreAuthenticationSuccessDataResponse(properties);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessDesensitizeDataResponse.class)
    public AuthenticationSuccessDesensitizeDataResponse authenticationSuccessDesensitizeDataResponse(AuthenticationProperties properties) {
        return new AuthenticationSuccessDesensitizeDataResponse(properties);
    }
}
