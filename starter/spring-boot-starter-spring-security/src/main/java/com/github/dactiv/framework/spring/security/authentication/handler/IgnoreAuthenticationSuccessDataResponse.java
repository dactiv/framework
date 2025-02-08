package com.github.dactiv.framework.spring.security.authentication.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import java.util.List;

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
        DocumentContext documentContext = createDocumentContext(details);
        properties.forEach(documentContext::delete);
        result.setData(documentContext.json());
    }

    public static DocumentContext createDocumentContext(Object data) {
        JsonNode rootNode = Casts.getObjectMapper().valueToTree(data);
        JsonNode filteredNode = rootNode.deepCopy();

        Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

        return JsonPath.using(conf).parse(filteredNode.toString());
    }

}
