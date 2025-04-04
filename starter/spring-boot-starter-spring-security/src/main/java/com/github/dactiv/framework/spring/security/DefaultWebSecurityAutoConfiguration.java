package com.github.dactiv.framework.spring.security;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.security.plugin.Plugin;
import com.github.dactiv.framework.spring.security.authentication.*;
import com.github.dactiv.framework.spring.security.authentication.adapter.WebSecurityConfigurerAfterAdapter;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.github.dactiv.framework.spring.security.authentication.config.RememberMeProperties;
import com.github.dactiv.framework.spring.security.authentication.provider.TypeRememberMeAuthenticationProvider;
import com.github.dactiv.framework.spring.security.authentication.service.PersistentTokenRememberMeUserDetailsService;
import com.github.dactiv.framework.spring.security.authentication.service.TypeTokenBasedRememberMeServices;
import com.github.dactiv.framework.spring.security.plugin.PluginSourceAuthorizationManager;
import com.github.dactiv.framework.spring.security.session.JsonSessionInformationExpiredStrategy;
import com.github.dactiv.framework.spring.web.result.error.ErrorResultResolver;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * spring security 配置实现
 *
 * @author maurice.chen
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties({AuthenticationProperties.class, RememberMeProperties.class})
@ConditionalOnProperty(prefix = "dactiv.authentication.spring.security", value = "enabled", matchIfMissing = true)
public class DefaultWebSecurityAutoConfiguration {

    private final AccessTokenContextRepository accessTokenContextRepository;

    private final AuthenticationProperties authenticationProperties;

    private final RememberMeProperties rememberMeProperties;

    private final List<WebSecurityConfigurerAfterAdapter> webSecurityConfigurerAfterAdapters;

    private final List<ErrorResultResolver> resultResolvers;

    private final TypeRememberMeAuthenticationProvider typeRememberMeAuthenticationProvider;

    private final TypeTokenBasedRememberMeServices typeSecurityPrincipalManager;

    private final JsonSessionInformationExpiredStrategy jsonSessionInformationExpiredStrategy;

    private final SessionRegistry sessionRegistry;

    public DefaultWebSecurityAutoConfiguration(AccessTokenContextRepository accessTokenContextRepository,
                                               AuthenticationProperties authenticationProperties,
                                               RememberMeProperties rememberMeProperties,
                                               TypeRememberMeAuthenticationProvider typeRememberMeAuthenticationProvider,
                                               TypeTokenBasedRememberMeServices typeSecurityPrincipalManager,
                                               ObjectProvider<ErrorResultResolver> errorResultResolvers,
                                               ObjectProvider<WebSecurityConfigurerAfterAdapter> webSecurityConfigurerAfterAdapter,
                                               JsonSessionInformationExpiredStrategy jsonSessionInformationExpiredStrategy,
                                               SessionRegistry sessionRegistry) {
        this.accessTokenContextRepository = accessTokenContextRepository;
        this.authenticationProperties = authenticationProperties;
        this.rememberMeProperties = rememberMeProperties;
        this.typeRememberMeAuthenticationProvider = typeRememberMeAuthenticationProvider;
        this.typeSecurityPrincipalManager = typeSecurityPrincipalManager;
        this.webSecurityConfigurerAfterAdapters = webSecurityConfigurerAfterAdapter.stream().collect(Collectors.toList());
        this.resultResolvers = errorResultResolvers.stream().collect(Collectors.toList());
        this.jsonSessionInformationExpiredStrategy = jsonSessionInformationExpiredStrategy;
        this.sessionRegistry = sessionRegistry;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(a -> a
                        .requestMatchers(authenticationProperties.getPermitUriAntMatchers().toArray(new String[0]))
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(b -> b
                        .securityContextRepository(new RequestAttributeSecurityContextRepository())
                        .authenticationDetailsSource(new AuditAuthenticationDetailsSource(authenticationProperties))
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .exceptionHandling(c -> c
                        .accessDeniedHandler(this.forbiddenAccessDeniedHandler())
                        .authenticationEntryPoint(new RestResultAuthenticationEntryPoint(resultResolvers))
                )
                .cors(c -> c.configure(httpSecurity))
                .csrf(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .maximumSessions(authenticationProperties.getMaximumSessions())
                        .sessionRegistry(sessionRegistry)
                        .expiredSessionStrategy(jsonSessionInformationExpiredStrategy)
                )
                .securityContext(s -> s.securityContextRepository(accessTokenContextRepository));

        if (rememberMeProperties.isEnabled()) {
            createRememberMeSetting(httpSecurity);
        }

        if (CollectionUtils.isNotEmpty(webSecurityConfigurerAfterAdapters)) {
            for (WebSecurityConfigurerAfterAdapter a : webSecurityConfigurerAfterAdapters) {
                a.configure(httpSecurity);
            }
        }

        httpSecurity.addFilterBefore(new IpAuthenticationFilter(this.authenticationProperties), UsernamePasswordAuthenticationFilter.class);

        SecurityFilterChain securityFilterChain = httpSecurity.build();

        RememberMeServices rememberMeServices = httpSecurity.getSharedObject(RememberMeServices.class);
        if (Objects.nonNull(rememberMeServices) && AbstractRememberMeServices.class.isAssignableFrom(rememberMeServices.getClass())) {
            AbstractRememberMeServices abstractRememberMeServices = Casts.cast(rememberMeServices);
            abstractRememberMeServices.setAuthenticationDetailsSource(new RememberMeAuthenticationDetailsSource());
        }

        return securityFilterChain;
    }

    private AccessDeniedHandler forbiddenAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            RestResult<?> result = RestResult.of(accessDeniedException.getMessage(), HttpStatus.FORBIDDEN.value(), String.valueOf(HttpStatus.FORBIDDEN.value()));
            response.getWriter().write(Casts.getObjectMapper().writeValueAsString(result));
        };
    }

    private void createRememberMeSetting(HttpSecurity httpSecurity) throws Exception {
        try {
            PersistentTokenRepository tokenRepository = httpSecurity
                    .getSharedObject(ApplicationContext.class)
                    .getBean(PersistentTokenRepository.class);

            httpSecurity
                    .rememberMe(r -> r
                            .userDetailsService(new PersistentTokenRememberMeUserDetailsService())
                            .alwaysRemember(rememberMeProperties.isAlways())
                            .rememberMeCookieName(rememberMeProperties.getCookieName())
                            .tokenValiditySeconds(rememberMeProperties.getTokenValiditySeconds())
                            .tokenRepository(tokenRepository)
                            .rememberMeCookieDomain(rememberMeProperties.getDomain())
                            .rememberMeParameter(rememberMeProperties.getParamName())
                            .useSecureCookie(rememberMeProperties.isUseSecureCookie())
                            .key(rememberMeProperties.getKey())
                    );
        } catch (Exception e){
            httpSecurity.rememberMe(r -> r.rememberMeServices(typeSecurityPrincipalManager));
        }
        httpSecurity.authenticationProvider(typeRememberMeAuthenticationProvider);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            if (CollectionUtils.isNotEmpty(webSecurityConfigurerAfterAdapters)) {
                for (WebSecurityConfigurerAfterAdapter a : webSecurityConfigurerAfterAdapters) {
                    a.configure(web);
                }
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsManager);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    @ConditionalOnMissingBean(PluginSourceAuthorizationManager.class)
    public PluginSourceAuthorizationManager pluginSourceAuthorizationManager(AuthenticationProperties authenticationProperties) {
        return new PluginSourceAuthorizationManager(authenticationProperties);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor pluginAuthorizationMethodInterceptor(ObjectProvider<SecurityContextHolderStrategy> strategyProvider,
                                                        PluginSourceAuthorizationManager pluginSourceAuthorizationManager,
                                                        ObjectProvider<AuthorizationEventPublisher> eventPublisherProvider) {


        AuthorizationManagerBeforeMethodInterceptor interceptor = new AuthorizationManagerBeforeMethodInterceptor(
                new AnnotationMatchingPointcut(null, Plugin.class, true),
                pluginSourceAuthorizationManager
        );

        interceptor.setOrder(AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder() - BigDecimal.ONE.intValue());
        strategyProvider.ifAvailable(interceptor::setSecurityContextHolderStrategy);
        eventPublisherProvider.ifAvailable(interceptor::setAuthorizationEventPublisher);

        return interceptor;
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler authenticationSuccessTokenTrustResolverExpressionHandler(ObjectProvider<GrantedAuthorityDefaults> defaultsProvider,
                                                                                                           ApplicationContext context) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setTrustResolver(new AuthenticationSuccessTokenTrustResolver());
        defaultsProvider.ifAvailable((d) -> handler.setDefaultRolePrefix(d.getRolePrefix()));
        handler.setApplicationContext(context);
        return handler;
    }
}
