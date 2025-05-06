package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.page.Page;
import com.github.dactiv.framework.commons.page.PageRequest;
import com.github.dactiv.framework.commons.page.TotalPage;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.config.SignTaskConfig;
import com.github.dactiv.framework.fadada.domain.body.task.*;
import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.task.SignTaskFilterMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.task.SignTaskIdMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class SignTaskService extends FadadaBasicService {

    private final AuthService authService;

    private final SignTaskConfig signTaskConfig;

    public SignTaskService(FadadaConfig fadadaConfig, SignTaskConfig signTaskConfig, AuthService authService, RestTemplate restTemplate) {
        super(fadadaConfig, restTemplate);
        this.authService = authService;
        this.signTaskConfig = signTaskConfig;
    }

    public CreateSignTaskResponseBody createSignTaskWithTemplate(CreateSignTaskWithTemplateRequestBody body) {
        if (StringUtils.isEmpty(body.getExpiresTime()) && Objects.nonNull(signTaskConfig.getExpiresTime())) {
            LocalDateTime expiresTime = LocalDateTime
                    .now()
                    .plus(signTaskConfig.getExpiresTime().getValue(), signTaskConfig.getExpiresTime().getUnit().toChronoUnit());
            body.setExpiresTime(String.valueOf(Date.from(expiresTime.atZone(ZoneId.systemDefault()).toInstant()).getTime()));
        }

        if (Objects.isNull(body.getAutoStart())) {
            body.setAutoStart(signTaskConfig.getAutoStart());
        }

        if (Objects.isNull(body.getAutoFillFinalize())) {
            body.setAutoFillFinalize(signTaskConfig.getAutoStart());
        }

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

    public SignTaskDetailResponseBody getSignTask(SignTaskIdMetadata signTaskId) {
        Map<String, Object> param = Casts.convertValue(signTaskId, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/sign-task/app/get-detail", authService.getCacheAccessToken().getToken(), param, SignTaskDetailResponseBody.class);
    }

    public void cancelSignTask(SignTaskIdMetadata signTaskId) {
        Map<String, Object> param = Casts.convertValue(signTaskId, Casts.MAP_TYPE_REFERENCE);
        executeApi("/sign-task/cancel", authService.getCacheAccessToken().getToken(), param, Void.class);
    }

    public void deleteSignTask(SignTaskIdMetadata signTaskId) {
        Map<String, Object> param = Casts.convertValue(signTaskId, Casts.MAP_TYPE_REFERENCE);
        executeApi("/sign-task/delete", authService.getCacheAccessToken().getToken(), param, Void.class);
    }

    public void extensionSignTask(ExtensionSignTaskRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        executeApi("/sign-task/extension", authService.getCacheAccessToken().getToken(), param, Void.class);
    }

}
