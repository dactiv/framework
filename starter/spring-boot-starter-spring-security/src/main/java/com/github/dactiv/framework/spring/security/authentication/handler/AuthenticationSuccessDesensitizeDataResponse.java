package com.github.dactiv.framework.spring.security.authentication.handler;


import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.commons.jackson.serializer.DesensitizeSerializer;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.jayway.jsonpath.DocumentContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Objects;

/**
 * 认证成功后的字段数据加 * 处理
 *
 * @author maurice.chen
 */
public class AuthenticationSuccessDesensitizeDataResponse implements JsonAuthenticationSuccessResponse {

    private final AuthenticationProperties authenticationProperties;

    public AuthenticationSuccessDesensitizeDataResponse(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public void setting(RestResult<Object> result, HttpServletRequest request) {

        List<String> properties = authenticationProperties
                .getAuthenticationSuccessDesensitizeDataMap()
                .get(request.getRequestURI().replace(AntPathMatcher.DEFAULT_PATH_SEPARATOR, StringUtils.EMPTY));
        if (CollectionUtils.isEmpty(properties)) {
            return ;
        }

        Object details = result.getData();
        DocumentContext documentContext = IgnoreAuthenticationSuccessDataResponse.createDocumentContext(details);

        for (String property : properties) {
            Object value = documentContext.read(property);
            if (Objects.isNull(value)) {
                continue;
            }
            documentContext.set(property, DesensitizeSerializer.desensitize(value.toString()));
        }

        result.setData(documentContext.json());
    }

}
