package com.github.dactiv.framework.spring.security;

import com.github.dactiv.framework.captcha.CaptchaAutoConfiguration;
import com.github.dactiv.framework.captcha.CaptchaProperties;
import com.github.dactiv.framework.captcha.DelegateCaptchaService;
import com.github.dactiv.framework.captcha.intercept.Interceptor;
import com.github.dactiv.framework.spring.security.controller.CaptchaExtendController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 验证码扩展配置
 *
 * @author maurice.chen
 */
@Configuration
@AutoConfigureAfter(CaptchaAutoConfiguration.class)
@EnableConfigurationProperties(CaptchaProperties.class)
@ConditionalOnClass(SpringSecurityAutoConfiguration.class)
@ConditionalOnProperty(prefix = "healthan.captcha", value = "enabled", matchIfMissing = true)
public class CaptchaExtendAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "healthan.captcha.controller", value = "enabled", matchIfMissing = true)
    public CaptchaExtendController captchaExtendController(@Lazy Interceptor interceptor,
                                                           CaptchaProperties captchaProperties,
                                                           DelegateCaptchaService delegateCaptchaService) {
        return new CaptchaExtendController(interceptor, captchaProperties, delegateCaptchaService);
    }
}
