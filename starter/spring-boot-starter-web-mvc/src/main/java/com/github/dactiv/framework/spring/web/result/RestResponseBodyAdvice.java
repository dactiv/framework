package com.github.dactiv.framework.spring.web.result;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.commons.exception.ErrorCodeException;
import com.github.dactiv.framework.security.filter.result.IgnoreOrDesensitizeResultHolder;
import com.github.dactiv.framework.spring.web.SpringWebMvcProperties;
import com.github.dactiv.framework.spring.web.mvc.SpringMvcUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * rest 响应同一格式实现类
 *
 * @author maurice.chen
 */
@ControllerAdvice
public class RestResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseBodyAdvice.class);

    /**
     * 默认的请求客户端头名称
     */
    private static final String DEFAULT_CLIENT_HEADER_NAME = "X-REQUEST-CLIENT";

    /**
     * 不需要格式化的属性名称
     */
    public static final String DEFAULT_NOT_FORMAT_ATTR_NAME = "X-REST-RESULT-NOT-FORMAT";

    /**
     * gateway 过来的 微服务前缀
     */
    public static final String DEFAULT_FORWARDED_PREFIX_HEADER_NAME = "X-FORWARDED-PREFIX";

    /**
     * 默认支持的客户端类型集合
     */
    public static final List<String> DEFAULT_SUPPORT_CLIENT = Collections.singletonList("SPRING_GATEWAY");

    private final SpringWebMvcProperties properties;

    public RestResponseBodyAdvice(SpringWebMvcProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        List<String> clients = request.getHeaders().get(DEFAULT_CLIENT_HEADER_NAME);

        // 获取是否执行控制器的某个方法遇到错误走以下流程，该值在 RestResultErrorAttributes 中设置，
        // 仅仅是为了不跟 RestResultErrorAttributes 冲突而已
        Boolean errorExecute = SpringMvcUtils.getRequestAttribute(RestResultErrorAttributes.DEFAULT_ERROR_EXECUTE_ATTR_NAME);
        // 从请求属性中获取是否不需要格式发，
        Boolean notFormat = SpringMvcUtils.getRequestAttribute(DEFAULT_NOT_FORMAT_ATTR_NAME);

        ServletServerHttpRequest httpRequest = Casts.cast(request);
        ServletServerHttpResponse httpResponse = Casts.cast(response);

        // 如果为空，在 headers 中获取是否存在不需要格式化字段，兼容两个模式
        if (notFormat == null || !notFormat) {
            List<String> list = request.getHeaders().get(DEFAULT_NOT_FORMAT_ATTR_NAME);

            if (CollectionUtils.isNotEmpty(list)) {
                notFormat = BooleanUtils.toBoolean(list.iterator().next());
            } else {
                list = response.getHeaders().get(DEFAULT_NOT_FORMAT_ATTR_NAME);
                if (CollectionUtils.isNotEmpty(list)) {
                    notFormat = BooleanUtils.toBoolean(list.iterator().next());
                }
            }

        }

        // 判断是否执行格式化
        boolean execute = (notFormat == null || !notFormat) && (errorExecute == null || !errorExecute);

        // 判断是否支持格式发，目前针对只有头的 X-REQUEST-CLIENT = supportClients 变量集合才会格式化
        boolean support = properties.isAllRestResultFormat() || (clients != null && clients.stream().anyMatch(properties::isSupportClient));

        if (support && execute && MediaType.APPLICATION_JSON.isCompatibleWith(selectedContentType)) {
            HttpStatus status = HttpStatus.valueOf(httpResponse.getServletResponse().getStatus());
            // 获取执行状态
            String message = status.getReasonPhrase();

            RestResult<Object> result;

            // 如果响应的 body 有值，并且是 RestResult，获取 data信息，看看是否需要过滤字段
            if (Objects.nonNull(body) && RestResult.class.isAssignableFrom(body.getClass())) {
                result = Casts.cast(body);
            } else {
                // 获取实际要响应的 data 内容
                Object data = Objects.isNull(body) ? new LinkedHashMap<>() : body;
                result = RestResult.of(message, status.value(), RestResult.SUCCESS_EXECUTE_CODE, data);
            }

            // 如果没设置执行代码。根据状态值来设置执行代码
            if (Objects.isNull(result.getExecuteCode())) {
                if (HttpStatus.OK == status) {
                    result.setExecuteCode(RestResult.SUCCESS_EXECUTE_CODE);
                } else {
                    result.setExecuteCode(ErrorCodeException.DEFAULT_EXCEPTION_CODE);
                }
            }

            response.setStatusCode(HttpStatus.valueOf(result.getStatus()));
            String url = httpRequest.getServletRequest().getRequestURI();
            String prefix = httpRequest.getHeaders().getFirst(DEFAULT_FORWARDED_PREFIX_HEADER_NAME);
            if (StringUtils.isNotEmpty(prefix)) {
                url = prefix + url;
            }
            result.getMetadata().put(RestResult.DEFAULT_URL_NAME, url);
            return IgnoreOrDesensitizeResultHolder.convert(result);
        } else {
            return IgnoreOrDesensitizeResultHolder.convert(body);
        }

    }

}
