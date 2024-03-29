package com.github.dactiv.framework.spring.security;


import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.crypto.algorithm.Base64;
import com.github.dactiv.framework.crypto.algorithm.cipher.RsaCipherService;
import com.github.dactiv.framework.spring.security.authentication.RedissonOAuth2AuthorizationService;
import com.github.dactiv.framework.spring.security.authentication.adapter.OAuth2AuthorizationConfigurerAdapter;
import com.github.dactiv.framework.spring.security.authentication.adapter.OAuth2WebSecurityConfigurerAfterAdapter;
import com.github.dactiv.framework.spring.security.authentication.config.OAuth2Properties;
import com.github.dactiv.framework.spring.security.authentication.config.SpringSecurityProperties;
import com.github.dactiv.framework.spring.security.authentication.handler.*;
import com.github.dactiv.framework.spring.security.authentication.oidc.OidcUserInfoAuthenticationMapper;
import com.github.dactiv.framework.spring.security.authentication.oidc.OidcUserInfoAuthenticationResolver;
import com.github.dactiv.framework.spring.web.result.error.ErrorResultResolver;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@AutoConfigureBefore(SpringSecurityAutoConfiguration.class)
@EnableConfigurationProperties({OAuth2Properties.class, SpringSecurityProperties.class})
@ConditionalOnClass(OAuth2AuthorizationServerConfigurer.class)
@ConditionalOnProperty(prefix = "dactiv.spring.security.oauth2", value = "enabled", havingValue = "true")
public class OAuth2WebSecurityAutoConfiguration {

    private static final RsaCipherService cipherService = new RsaCipherService();

    @Bean
    @ConditionalOnMissingBean(JsonAuthenticationFailureHandler.class)
    public JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler(ObjectProvider<JsonAuthenticationFailureResponse> failureResponse,
                                                                             OAuth2Properties oAuth2Properties,
                                                                             SpringSecurityProperties springSecurityProperties) {

        List<AntPathRequestMatcher> list = new LinkedList<>(oAuth2Properties.getOauth2Urls());
        list.add(new AntPathRequestMatcher(springSecurityProperties.getLoginProcessingUrl(), HttpMethod.POST.name()));

        return new JsonAuthenticationFailureHandler(
                failureResponse.orderedStream().collect(Collectors.toList()),
                list
        );
    }

    @Bean
    @ConditionalOnMissingBean(JsonAuthenticationSuccessHandler.class)
    public JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler(ObjectProvider<JsonAuthenticationSuccessResponse> successResponse,
                                                                             OAuth2Properties oAuth2Properties,
                                                                             SpringSecurityProperties springSecurityProperties) {

        List<AntPathRequestMatcher> list = new LinkedList<>(oAuth2Properties.getOauth2Urls());
        list.add(new AntPathRequestMatcher(springSecurityProperties.getLoginProcessingUrl(), HttpMethod.POST.name()));

        return new JsonAuthenticationSuccessHandler(
                successResponse.orderedStream().collect(Collectors.toList()),
                list
        );
    }

    /**
     * jwk source 配置
     *
     * @return JWKSource
     */
    @Bean
    @ConditionalOnMissingBean(JWKSource.class)
    public JWKSource<SecurityContext> jwkSource(OAuth2Properties oAuth2Properties) {
        PublicKey publicKey = cipherService.getPublicKey(Base64.decode(oAuth2Properties.getPublicKey()));
        PrivateKey privateKey = cipherService.getPrivateKey(Base64.decode(oAuth2Properties.getPrivateKey()));

        RSAPublicKey rsaPublicKey = Casts.cast(publicKey);
        RSAPrivateKey rsaPrivateKey = Casts.cast(privateKey);

        RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .keyID(oAuth2Properties.getKeyId())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    @ConditionalOnMissingBean(OAuth2JsonAuthenticationSuccessResponse.class)
    public OAuth2JsonAuthenticationSuccessResponse oAuth2JsonAuthenticationSuccessResponse(OAuth2Properties oAuth2Properties) {
        return new OAuth2JsonAuthenticationSuccessResponse(oAuth2Properties);
    }

    /**
     * 配置jwt解析器
     *
     * @param jwkSource jwk源
     * @return JwtDecoder
     */
    @Bean
    @ConditionalOnMissingBean(JwtDecoder.class)
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 添加认证服务器配置，设置jwt签发者、默认端点请求地址等
     *
     * @return AuthorizationServerSettings
     */
    @Bean
    @ConditionalOnMissingBean(AuthorizationServerSettings.class)
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    @ConditionalOnMissingBean(OidcUserInfoAuthenticationMapper.class)
    public OidcUserInfoAuthenticationMapper oidcUserInfoAuthenticationMapper(ObjectProvider<OidcUserInfoAuthenticationResolver> oidcUserInfoAuthenticationResolvers){
        return new OidcUserInfoAuthenticationMapper(oidcUserInfoAuthenticationResolvers.orderedStream().collect(Collectors.toList()));
    }

    @Bean
    @ConditionalOnMissingBean(RedissonOAuth2AuthorizationService.class)
    public RedissonOAuth2AuthorizationService redissonOAuth2AuthorizationService(RedissonClient redissonClient,
                                                                                 OAuth2Properties oAuth2Properties) {
        return new RedissonOAuth2AuthorizationService(redissonClient, oAuth2Properties);
    }

    @Bean
    @ConditionalOnMissingBean(OAuth2WebSecurityConfigurerAfterAdapter.class)
    public OAuth2WebSecurityConfigurerAfterAdapter oAuth2WebSecurityConfigurerAfterAdapter(JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler,
                                                                                           JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler,
                                                                                           OAuth2Properties oAuth2Properties,
                                                                                           OidcUserInfoAuthenticationMapper oidcUserInfoAuthenticationMapper,
                                                                                           ObjectProvider<OAuth2AuthorizationConfigurerAdapter> oAuth2AuthorizationConfigurerAdapters,
                                                                                           ObjectProvider<ErrorResultResolver> resultResolvers) {
        return new OAuth2WebSecurityConfigurerAfterAdapter(
                jsonAuthenticationFailureHandler,
                jsonAuthenticationSuccessHandler,
                oidcUserInfoAuthenticationMapper,
                oAuth2AuthorizationConfigurerAdapters.orderedStream().collect(Collectors.toList()),
                resultResolvers.orderedStream().collect(Collectors.toList()),
                oAuth2Properties
        );
    }
}
