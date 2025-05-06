package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.page.Page;
import com.github.dactiv.framework.commons.page.PageRequest;
import com.github.dactiv.framework.commons.page.TotalPage;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.body.template.*;
import com.github.dactiv.framework.fadada.domain.body.template.doc.*;
import com.github.dactiv.framework.fadada.domain.body.template.sign.*;
import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.template.DocTemplateFilterMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.template.SignTemplateFilterMetadata;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class TemplateService extends FadadaBasicService {

    private final AuthService authService;

    public TemplateService(FadadaConfig fadadaConfig, RestTemplate restTemplate, AuthService authService) {
        super(fadadaConfig, restTemplate);
        this.authService = authService;
    }

    public GetTemplateEditUrlResponseBody getTemplateEditUrl(GetTemplateEditUrlRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/template/edit/get-url", authService.getCacheAccessToken().getToken(), param, GetTemplateEditUrlResponseBody.class);
    }

    public GetTemplateCreateUrlResponseBody getTemplateCreateUrl(GetTemplateCreateUrlRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/template/create/get-url", authService.getCacheAccessToken().getToken(), param, GetTemplateCreateUrlResponseBody.class);
    }

    public GetTemplatePreviewUrlResponseBody getTemplatePreviewUrl(GetTemplatePreviewUrlRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/template/preview/get-url", authService.getCacheAccessToken().getToken(), param, GetTemplatePreviewUrlResponseBody.class);
    }

    // ------------------------------------------------ 文件模版管理 ------------------------------------------------ //

    public CreateDocTemplateResponseBody createDocTemplate(CreateDocTemplateRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/doc-template/create", authService.getCacheAccessToken().getToken(), param, CreateDocTemplateResponseBody.class);
    }

    public CopyCreateDocTemplateResponseBody copyDocTemplate(CopyCreateDocTemplateRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/doc-template/copy-create", authService.getCacheAccessToken().getToken(), param, CopyCreateDocTemplateResponseBody.class);
    }

    public void deleteDocTemplate(DeleteDocTemplateRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        executeApi(" /doc-template/delete", authService.getCacheAccessToken().getToken(), param, Void.class);
    }

    public Page<DocTemplateResponseBody> searchDocTemplatePage(PageRequest pageRequest,
                                                                OpenIdMetadata owner,
                                                                DocTemplateFilterMetadata filter) {
        SearchDocTemplatePageRequestBody body = new SearchDocTemplatePageRequestBody(owner, filter);
        body.setListPageNo(pageRequest.getNumber());
        body.setListPageSize(pageRequest.getSize());

        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        DocTemplatePageResponseBody response = executeApi("/doc-template/get-list", authService.getCacheAccessToken().getToken(), param, DocTemplatePageResponseBody.class);

        return new TotalPage<>(pageRequest, response.getDocTemplates(), response.getTotalCount());
    }

    public void setDocTemplateStatus(SetDocTemplateStatusRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        executeApi("/doc-template/set-status", authService.getCacheAccessToken().getToken(), param, Void.class);
    }

    // ------------------------------------------------ 签署模版管理 ------------------------------------------------ //

    public Page<SignTemplateResponseBody> searchSignTemplatePage(PageRequest pageRequest,
                                                                 OpenIdMetadata owner,
                                                                 SignTemplateFilterMetadata filter) {
        SearchSignTemplatePageRequestBody body = new SearchSignTemplatePageRequestBody(owner, filter);
        body.setListPageNo(pageRequest.getNumber());
        body.setListPageSize(pageRequest.getSize());

        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        SignTemplatePageResponseBody response = executeApi("/sign-template/get-list", authService.getCacheAccessToken().getToken(), param, SignTemplatePageResponseBody.class);

        return new TotalPage<>(pageRequest, response.getSignTemplates(), response.getTotalCount());
    }

    public void deleteSignTemplate(DeleteSignTemplateRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        executeApi("/sign-template/delete", authService.getCacheAccessToken().getToken(), param, Void.class);
    }

    public CopyCreateSignTemplateResponseBody copySignTemplate(CopyCreateSignTemplateRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/sign-template/copy-create", authService.getCacheAccessToken().getToken(), param, CopyCreateSignTemplateResponseBody.class);
    }

    public void setSignTemplateStatus(SetSignTemplateStatusRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        executeApi("/sign-template/set-status", authService.getCacheAccessToken().getToken(), param, Void.class);
    }

    public Map<String, Object> getSignTemplateDetail(SignTemplateDetailRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/sign-template/get-detail", authService.getCacheAccessToken().getToken(), param, Map.class);
    }

}
