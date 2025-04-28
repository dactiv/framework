package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.body.template.CreateDocTemplateRequestBody;
import com.github.dactiv.framework.fadada.domain.body.template.CreateDocTemplateResponseBody;
import com.github.dactiv.framework.fadada.domain.body.template.GetTemplateEditUrlRequestBody;
import com.github.dactiv.framework.fadada.domain.body.template.GetTemplateEditUrlResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class TemplateService extends FadadaBasicService{

    private final AuthService authService;

    public TemplateService(FadadaConfig fadadaConfig, RestTemplate restTemplate, AuthService authService) {
        super(fadadaConfig, restTemplate);
        this.authService = authService;
    }

    public CreateDocTemplateResponseBody createTemplate(CreateDocTemplateRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/doc-template/create", authService.getCacheAccessToken().getToken(), param, CreateDocTemplateResponseBody.class);
    }

    public GetTemplateEditUrlResponseBody getTemplateEditUrl(GetTemplateEditUrlRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/template/edit/get-url", authService.getCacheAccessToken().getToken(), param, GetTemplateEditUrlResponseBody.class);
    }
}
