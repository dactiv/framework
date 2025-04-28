package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.body.user.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class UserService extends FadadaBasicService {

    private final AuthService authService;

    public UserService(FadadaConfig fadadaConfig, AuthService authService, RestTemplate restTemplate) {
        super(fadadaConfig, restTemplate);
        this.authService = authService;
    }

    public AuthUrlResponseBody getUserAuthUrl(GetUserAuthUrlRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/user/get-auth-url", authService.getCacheAccessToken().getToken(), param, AuthUrlResponseBody.class);
    }

    public UserAuthStatusResponseBody getUserAuthStatus(GetUserAutStatusRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/user/get", authService.getCacheAccessToken().getToken(), param, UserAuthStatusResponseBody.class);
    }

    public UserResponseBody getUserByClientUserId(String clientUserId) {
        UserAuthStatusResponseBody responseBody = getUserAuthStatus(new GetUserAutStatusRequestBody(clientUserId));
        return getUser(new GetUserRequestBody(responseBody.getOpenUserId()));
    }

    public UserResponseBody getUser(GetUserRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/user/get-identity-info", authService.getCacheAccessToken().getToken(), param, UserResponseBody.class);
    }
}
