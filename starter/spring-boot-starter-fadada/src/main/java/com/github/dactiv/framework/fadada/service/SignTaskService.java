package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.body.task.CreateSignTaskResponseBody;
import com.github.dactiv.framework.fadada.domain.body.task.CreateSignTaskWithTemplateRequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class SignTaskService extends FadadaBasicService {

    private final AuthService authService;

    public SignTaskService(FadadaConfig fadadaConfig, AuthService authService, RestTemplate restTemplate) {
        super(fadadaConfig, restTemplate);
        this.authService = authService;
    }

    public CreateSignTaskResponseBody createSignTaskWithTemplate(CreateSignTaskWithTemplateRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/sign-task/create-with-template", authService.getCacheAccessToken().getToken(), param, CreateSignTaskResponseBody.class);
    }
}
