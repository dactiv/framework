package com.github.dactiv.framework.spring.security.authentication.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Objects;

/**
 * 认证成功后的忽略相应字段处理
 *
 * @author maurice.chen
 */
public class IgnoreAuthenticationSuccessDataResponse implements JsonAuthenticationSuccessResponse {

    private final AuthenticationProperties authenticationProperties;

    public IgnoreAuthenticationSuccessDataResponse(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public void setting(RestResult<Object> result, HttpServletRequest request) {
        List<String> properties = authenticationProperties
                .getIgnoreAuthenticationSuccessDataMap()
                .get(request.getRequestURI().replace(AntPathMatcher.DEFAULT_PATH_SEPARATOR, StringUtils.EMPTY));
        if (CollectionUtils.isEmpty(properties)) {
            return ;
        }

        Object details = result.getData();
        JsonNode rootNode = Casts.getObjectMapper().valueToTree(details);
        JsonNode filteredNode = rootNode.deepCopy();

        DocumentContext documentContext = JsonPath.parse(filteredNode.toString());
        for (String property : properties) {
            Object value = documentContext.read(property);
            if (Objects.isNull(value)) {
                continue;
            }
            documentContext.delete(property);
        }

        result.setData(documentContext.json());
    }

}
