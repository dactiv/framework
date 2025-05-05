package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.page.Page;
import com.github.dactiv.framework.commons.page.PageRequest;
import com.github.dactiv.framework.commons.page.TotalPage;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.body.task.*;
import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.task.SignTaskFilterMetadata;
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

    public SignTaskActorGetUrlResponseBody getSignTaskActorUrl(GetSignTaskActorUrlRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/sign-task/actor/get-url", authService.getCacheAccessToken().getToken(), param, SignTaskActorGetUrlResponseBody.class);
    }

    public Page<SignTaskResponseBody> searchSignTaskPage(PageRequest pageRequest,
                                                         OpenIdMetadata ownerId,
                                                         String ownerRole,
                                                         String pendingRole,
                                                         String memberId,
                                                         String catalogId,
                                                         SignTaskFilterMetadata filter) {
        SearchSignTaskPageRequestBody body = new SearchSignTaskPageRequestBody(ownerId, ownerRole, pendingRole, memberId, catalogId, filter);
        body.setListPageNo(pageRequest.getNumber());
        body.setListPageSize(pageRequest.getSize());
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        SignTaskPageResponseBody responseBody = executeApi("/sign-task/owner/get-list", authService.getCacheAccessToken().getToken(), param, SignTaskPageResponseBody.class);
        return new TotalPage<>(pageRequest, responseBody.getSignTasks(), responseBody.getTotalCount());
    }

}
