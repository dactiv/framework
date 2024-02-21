package com.github.dactiv.framework.spring.web.result.filter;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.spring.web.result.filter.annotation.Exclude;
import com.github.dactiv.framework.spring.web.result.filter.annotation.Excludes;
import com.github.dactiv.framework.spring.web.result.filter.annotation.view.ExcludeView;
import com.github.dactiv.framework.spring.web.result.filter.annotation.view.ExcludeViews;
import com.github.dactiv.framework.spring.web.result.filter.annotation.view.IncludeView;
import com.github.dactiv.framework.spring.web.result.filter.annotation.view.IncludeViews;
import com.github.dactiv.framework.spring.web.result.filter.holder.FilterResultHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * 控制器结果集响应的字段过滤器和拦截器的实现，用于结合 jackson 序列化针对自定义注解:
 *
 * <pre>
 *     {@link Exclude}
 *     {@link Excludes}
 *     {@link ExcludeView}
 *     {@link ExcludeViews}
 *     {@link IncludeView}
 *     {@link IncludeViews}
 * </pre>
 *
 * 的使用做一个前置和后置的支持
 *
 * @author maurice.chen
 */
public class ControllerRestResultFilterInterceptor extends GenericFilterBean implements AsyncHandlerInterceptor {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {

            FilterResultHolder.clear();

            if (logger.isTraceEnabled()) {
                logger.trace("清除线程绑定的 filter result 内容");
            }
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!HandlerMethod.class.isAssignableFrom(handler.getClass())) {
            return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = Casts.cast(handler);

        Exclude[] excludeArray = handlerMethod.getMethod().getAnnotationsByType(Exclude.class);
        if (ArrayUtils.isNotEmpty(excludeArray)) {
            FilterResultHolder.get().addAll(Arrays.stream(excludeArray).map(Exclude::value).toList());
        }

        Excludes excludes = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Excludes.class);
        if (Objects.nonNull(excludes)) {
            FilterResultHolder.get().addAll(Arrays.stream(excludes.value()).map(Exclude::value).toList());
        }

        IncludeView[] includeViewArray = handlerMethod.getMethod().getAnnotationsByType(IncludeView.class);
        if (ArrayUtils.isNotEmpty(includeViewArray)) {
            FilterResultHolder.get().addAll(Arrays.stream(includeViewArray).map(IncludeView::value).toList());
        }

        IncludeViews includeViews = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), IncludeViews.class);
        if (Objects.nonNull(includeViews)) {
            FilterResultHolder.get().addAll(Arrays.stream(includeViews.value()).map(IncludeView::value).toList());
        }

        ExcludeView[] excludeViewArray = handlerMethod.getMethod().getAnnotationsByType(ExcludeView.class);
        if (ArrayUtils.isNotEmpty(excludeViewArray)) {
            FilterResultHolder.get().addAll(Arrays.stream(excludeViewArray).map(ExcludeView::value).toList());
        }

        ExcludeViews excludeViews = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ExcludeViews.class);
        if (Objects.nonNull(excludeViews)) {
            FilterResultHolder.get().addAll(Arrays.stream(excludeViews.value()).map(ExcludeView::value).toList());
        }

        return true;
    }
}
