package com.github.dactiv.framework.spring.security.authentication.token;

import com.github.dactiv.framework.spring.security.entity.AuditAuthenticationDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.MultiValueMap;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 请求认证 token
 *
 * @author maurice
 */
public class RequestAuthenticationToken extends TypeAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 8070060147431763553L;

    private final MultiValueMap<String, String> parameterMap;

    private final Map<String, Object> metadata = new LinkedHashMap<>();

    public RequestAuthenticationToken(MultiValueMap<String, String> parameterMap,
                                      Object principal,
                                      Object credentials,
                                      String type) {
        super(principal, credentials, type);
        this.parameterMap = parameterMap;
    }

    public RequestAuthenticationToken(AuditAuthenticationDetails details, UsernamePasswordAuthenticationToken token) {
        super(token.getPrincipal(), token.getCredentials(), details.getType());
        WebAuthenticationDetails requestAuthenticationDetails = new WebAuthenticationDetails(
                details.getRemoteAddress(),
                details.getSessionId()
        );
        setDetails(requestAuthenticationDetails);
        parameterMap = details.getRequestParameters();
    }

    /**
     * 获取请求参数信息
     *
     * @return 请求参数信息
     */
    public MultiValueMap<String, String> getParameterMap() {
        return parameterMap;
    }

    /**
     * 获取附加元数据信息
     *
     * @return 元数据信息
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
