package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.fadada.domain.body.task.CreateSignTaskResponseBody;
import com.github.dactiv.framework.fadada.domain.body.task.CreateSignTaskWithTemplateRequestBody;
import com.github.dactiv.framework.fadada.domain.metadata.ActorMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.task.AddTaskActorMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class SignTaskServiceTest {

    private final SignTaskService signTaskService;

    @Autowired
    public SignTaskServiceTest(SignTaskService signTaskService) {
        this.signTaskService = signTaskService;
    }

    @Test
    public void testCreateSignTaskWithTemplate() {
        CreateSignTaskWithTemplateRequestBody body = new CreateSignTaskWithTemplateRequestBody();
        body.setSignTaskSubject("签署任务模板发起V5.1签署任务-个人1677138236");
        body.setInitiator(OpenIdMetadata.getInstance(OpenIdMetadata.CORP_TYPE_VALUE, signTaskService.getFadadaConfig().getOpenCorpId()));
        body.setSignTemplateId("1745824442782185394");
        body.setExpiresTime(String.valueOf(Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()).getTime()));
        body.setAutoStart(true);
        body.setAutoFillFinalize(true);
        body.setTransReferenceId(UUID.randomUUID().toString().replaceAll("-",""));
        AddTaskActorMetadata addTaskActor = new AddTaskActorMetadata();
        ActorMetadata actor = new ActorMetadata();
        actor.setActorId("参与方2");
        actor.setActorType(OpenIdMetadata.PERSON_TYPE_VALUE);
        actor.setActorName("陈小菠");
        actor.setPermissions(List.of("sign"));
        addTaskActor.setActor(actor);
        body.setActors(List.of(addTaskActor));

        CreateSignTaskResponseBody responseBody = signTaskService.createSignTaskWithTemplate(body);
        Assertions.assertTrue(StringUtils.isNotEmpty(responseBody.getSignTaskId()));
    }

}
