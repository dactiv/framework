package com.github.dactiv.framework.security;

import com.github.dactiv.framework.security.filter.result.IgnoreOrDesensitizeResultFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 安全配置自动配置
 *
 * @author maurice.chen
 */
@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class SecurityConfiguration {

    @Bean
    public FilterRegistrationBean<IgnoreOrDesensitizeResultFilter> ignoreOrDesensitizeResultFilter(WebProperties webProperties) {
        FilterRegistrationBean<IgnoreOrDesensitizeResultFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new IgnoreOrDesensitizeResultFilter(webProperties));
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }
}
