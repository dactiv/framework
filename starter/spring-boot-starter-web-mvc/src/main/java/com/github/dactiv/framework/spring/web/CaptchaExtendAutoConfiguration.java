package com.github.dactiv.framework.spring.web;

import com.github.dactiv.framework.captcha.CaptchaAutoConfiguration;
import com.github.dactiv.framework.captcha.CaptchaProperties;
import com.github.dactiv.framework.captcha.DelegateCaptchaService;
import com.github.dactiv.framework.captcha.intercept.Interceptor;
import com.github.dactiv.framework.captcha.storage.CaptchaStorageManager;
import com.github.dactiv.framework.captcha.tianai.TianaiCaptchaService;
import com.github.dactiv.framework.captcha.tianai.config.TianaiCaptchaProperties;
import com.github.dactiv.framework.spring.web.captcha.CaptchaController;
import com.github.dactiv.framework.spring.web.captcha.ControllerTianaiCaptchaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ResourceLoader;
import org.springframework.validation.Validator;

@Configuration
@ConditionalOnClass(CaptchaAutoConfiguration.class)
@AutoConfigureBefore(CaptchaAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CaptchaExtendAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "dactiv.captcha.controller", value = "enabled", matchIfMissing = true)
    public CaptchaController captchaController(@Lazy Interceptor interceptor,
                                               CaptchaProperties captchaProperties,
                                               TianaiCaptchaService captchaService,
                                               ResourceLoader resourceLoader,
                                               DelegateCaptchaService delegateCaptchaService) {
        return new CaptchaController(delegateCaptchaService, interceptor, resourceLoader, captchaProperties, captchaService);
    }

    @Bean
    @ConditionalOnMissingBean(TianaiCaptchaService.class)
    @ConditionalOnProperty(prefix = "dactiv.captcha", value = "enabled", matchIfMissing = true)
    public ControllerTianaiCaptchaService controllerTianaiCaptchaService(CaptchaProperties captchaProperties,
                                                                         @Qualifier("mvcValidator") Validator validator,
                                                                         @Lazy Interceptor interceptor,
                                                                         CaptchaStorageManager captchaStorageManager,
                                                                         TianaiCaptchaProperties tianaiCaptchaProperties) {
        return new ControllerTianaiCaptchaService(
                captchaProperties,
                validator,
                interceptor,
                captchaStorageManager,
                tianaiCaptchaProperties
        );
    }
}
