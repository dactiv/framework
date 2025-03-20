package com.github.dactiv.framework.security.filter.result;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.security.WebProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 忽略属性或脱敏属性结果过滤器
 *
 * @author maurice.chen
 */
public class IgnoreOrDesensitizeResultFilter extends OncePerRequestFilter {

    private final WebProperties webProperties;

    public IgnoreOrDesensitizeResultFilter(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
        try {
            String convert = request.getRequestURI().replace(AntPathMatcher.DEFAULT_PATH_SEPARATOR, Casts.UNDERSCORE);
            String target = Casts.castSnakeCaseToCamelCase(StringUtils.removeStart(convert, Casts.UNDERSCORE));

            List<String> ignoreProperties = webProperties
                    .getIgnoreResultMap()
                    .get(target);
            if (CollectionUtils.isNotEmpty(ignoreProperties)) {
                IgnoreOrDesensitizeResultHolder.setIgnoreProperties(ignoreProperties);
            }

            List<String> desensitizeProperties = webProperties
                    .getDesensitizeResultMap()
                    .get(target);
            if (CollectionUtils.isNotEmpty(desensitizeProperties)) {
                IgnoreOrDesensitizeResultHolder.setDesensitizeProperties(desensitizeProperties);
            }

            filterChain.doFilter(request, response);
        } finally {
            IgnoreOrDesensitizeResultHolder.clear();
        }
    }
}
