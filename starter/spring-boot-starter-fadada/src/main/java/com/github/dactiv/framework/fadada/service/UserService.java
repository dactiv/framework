package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.config.PersonAuthConfig;
import com.github.dactiv.framework.fadada.domain.body.user.AuthUrlResponseBody;
import com.github.dactiv.framework.fadada.domain.body.user.GetUserAuthUrlRequestBody;
import com.github.dactiv.framework.fadada.domain.body.user.GetUserIdentityResponseBody;
import com.github.dactiv.framework.fadada.domain.body.user.GetUserResponseBody;
import com.github.dactiv.framework.fadada.domain.metadata.user.OpenUserIdRequestMetdata;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

public class UserService extends FadadaBasicService {

    private final AuthService authService;

    private final PersonAuthConfig personAuthConfig;

    public UserService(FadadaConfig fadadaConfig, PersonAuthConfig personAuthConfig, AuthService authService, RestTemplate restTemplate) {
        super(fadadaConfig, restTemplate);
        this.authService = authService;
        this.personAuthConfig = personAuthConfig;
    }

    public AuthUrlResponseBody getUserAuthUrl(GetUserAuthUrlRequestBody body) {

        if (CollectionUtils.isEmpty(body.getAuthScopes())) {
            body.setAuthScopes(personAuthConfig.getAuthScopes());
        }

        if (CollectionUtils.isEmpty(body.getNonEditableInfo())) {
            body.setNonEditableInfo(personAuthConfig.getNonEditableInfo());
        }

        if (StringUtils.isEmpty(body.getUserIdentInfo().getFaceAuthMode())) {
            body.getUserIdentInfo().setFaceAuthMode(personAuthConfig.getFaceAuthMode());
        }

        if (StringUtils.isEmpty(body.getRedirectUrl())) {
            body.setRedirectUrl(URLEncoder.encode(personAuthConfig.getRedirectUrl(), Charset.defaultCharset()));
        }

        if (StringUtils.isEmpty(body.getRedirectMiniAppUrl())) {
            body.setRedirectMiniAppUrl(personAuthConfig.getRedirectMiniAppUrl());
        }

        if (Objects.isNull(body.getUnbindAccount())) {
            body.setUnbindAccount(personAuthConfig.isUnbindAccount());
        }

        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);

        if (StringUtils.isEmpty(body.getRedirectUrl())) {
            String redirectUrl = personAuthConfig.getRedirectUrl();

            if (StringUtils.contains(Casts.QUESTION_MARK, redirectUrl)) {

            }
            body.setRedirectUrl(personAuthConfig.getRedirectUrl());
        }

        return executeApi("/user/get-auth-url", authService.getAccessTokenIfCacheNull().getToken(), param, AuthUrlResponseBody.class);
    }

    public GetUserIdentityResponseBody getUserIdentity(OpenUserIdRequestMetdata body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/user/get-identity-info", authService.getAccessTokenIfCacheNull().getToken(), param, GetUserIdentityResponseBody.class);
    }

    public void disable(OpenUserIdRequestMetdata body)  {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        executeApi("/user/disable", authService.getAccessTokenIfCacheNull().getToken(), param, Void.class);
    }

    public void enable(OpenUserIdRequestMetdata body)  {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        executeApi("/user/enable", authService.getAccessTokenIfCacheNull().getToken(), param, Void.class);
    }

    public GetUserResponseBody getUser(OpenUserIdRequestMetdata body)  {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/user/get", authService.getAccessTokenIfCacheNull().getToken(), param, GetUserResponseBody.class);
    }

    public void unbind(OpenUserIdRequestMetdata body)  {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        executeApi("/user/unbind", authService.getAccessTokenIfCacheNull().getToken(), param, Void.class);
    }

    public PersonAuthConfig getPersonAuthConfig() {
        return personAuthConfig;
    }
}
