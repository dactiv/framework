package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.fadada.domain.body.task.CreateSignTaskResponseBody;
import com.github.dactiv.framework.fadada.domain.body.task.CreateSignTaskWithTemplateRequestBody;
import com.github.dactiv.framework.fadada.domain.body.task.GetSignTaskActorUrlRequestBody;
import com.github.dactiv.framework.fadada.domain.body.task.SignTaskActorGetUrlResponseBody;
import com.github.dactiv.framework.fadada.domain.metadata.ActorMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.task.AddTaskActorMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.task.OpenIdMetadata;
import com.github.dactiv.framework.fadada.enumerate.OpenIdType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class SignTaskServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SignTaskServiceTest.class);
    private final SignTaskService signTaskService;

    @Autowired
    public SignTaskServiceTest(SignTaskService signTaskService) {
        this.signTaskService = signTaskService;
    }

    @Test
    public void testCreateSignTaskWithTemplate() {
        CreateSignTaskWithTemplateRequestBody body = new CreateSignTaskWithTemplateRequestBody();
        body.setSignTaskSubject("签署任务模板发起V5.1签署任务-个人1677138236");
        body.setInitiator(OpenIdMetadata.getInstance(OpenIdType.CORP.getValue(), signTaskService.getFadadaConfig().getOpenCorpId()));
        body.setSignTemplateId("1745985574854194173");
        body.setExpiresTime(String.valueOf(Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()).getTime()));
        body.setAutoStart(true);
        body.setAutoFillFinalize(true);
        body.setTransReferenceId(UUID.randomUUID().toString().replaceAll("-",""));

        AddTaskActorMetadata addTaskActor1 = new AddTaskActorMetadata();
        ActorMetadata actor1 = new ActorMetadata();
        actor1.setActorId("甲方");
        actor1.setActorType(OpenIdType.PERSON.getValue());
        actor1.setActorName("陈小菠");
        actor1.setActorOpenId("784bf53e5f864aad89e363eaa0dea3a5");
        actor1.setPermissions(List.of("sign"));
        addTaskActor1.setActor(actor1);

        AddTaskActorMetadata addTaskActor2 = new AddTaskActorMetadata();
        ActorMetadata actor2 = new ActorMetadata();
        actor2.setActorId("乙方");
        actor2.setActorType(OpenIdType.PERSON.getValue());
        actor2.setActorName("李晚秋");
        actor2.setActorOpenId("4d95bd403fb8437aa424ee4cfbff2666");
        actor2.setPermissions(List.of("sign"));
        addTaskActor2.setActor(actor2);
        //actor.setActorOpenId("777cc772b4db4b638a42cb15bb296981");
        body.setActors(List.of(addTaskActor1,addTaskActor2));

        CreateSignTaskResponseBody responseBody = signTaskService.createSignTaskWithTemplate(body);
        Assertions.assertTrue(StringUtils.isNotEmpty(responseBody.getSignTaskId()));

        SignTaskActorGetUrlResponseBody body1 = signTaskService.getSignTaskActorUrl(new GetSignTaskActorUrlRequestBody(responseBody.getSignTaskId(), "甲方"));
        log.info(body1.getActorSignTaskUrl());
        SignTaskActorGetUrlResponseBody body2 = signTaskService.getSignTaskActorUrl(new GetSignTaskActorUrlRequestBody(responseBody.getSignTaskId(), "乙方"));
        log.info(body2.getActorSignTaskUrl());
        SignTaskActorGetUrlResponseBody body3 = signTaskService.getSignTaskActorUrl(new GetSignTaskActorUrlRequestBody(responseBody.getSignTaskId(), "海象爱家"));
        log.info(body3.getActorSignTaskUrl());
    }

}
