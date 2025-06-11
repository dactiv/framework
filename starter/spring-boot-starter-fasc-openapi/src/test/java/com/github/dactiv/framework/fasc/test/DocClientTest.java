package com.github.dactiv.framework.fasc.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.fasc.bean.base.BaseRes;
import com.github.dactiv.framework.fasc.bean.common.Actor;
import com.github.dactiv.framework.fasc.bean.common.Field;
import com.github.dactiv.framework.fasc.bean.common.FieldPosition;
import com.github.dactiv.framework.fasc.bean.common.OpenId;
import com.github.dactiv.framework.fasc.client.DocClient;
import com.github.dactiv.framework.fasc.client.SignTaskClient;
import com.github.dactiv.framework.fasc.config.FascConfig;
import com.github.dactiv.framework.fasc.enums.common.IdTypeEnum;
import com.github.dactiv.framework.fasc.enums.doc.FileTypeEnum;
import com.github.dactiv.framework.fasc.req.doc.FddFileUrl;
import com.github.dactiv.framework.fasc.req.doc.FileProcessReq;
import com.github.dactiv.framework.fasc.req.doc.GetUploadUrlReq;
import com.github.dactiv.framework.fasc.req.signtask.*;
import com.github.dactiv.framework.fasc.res.doc.FileProcessRes;
import com.github.dactiv.framework.fasc.res.doc.GetUploadUrlRes;
import com.github.dactiv.framework.fasc.res.signtask.CreateSignTaskRes;
import com.github.dactiv.framework.fasc.res.signtask.GetSignTaskPreviewUrlRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@SpringBootTest
public class DocClientTest {

    @Autowired
    private DocClient docClient;

    @Autowired
    private SignTaskClient signTaskClient;

    @Autowired
    private FascConfig config;

    @Test
    public void testGenerateSignTask() throws Exception {

        Casts.getObjectMapper().setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);

        File localFile = new File("C:/Users/olale/OneDrive/文档/陈小菠-Java-广州.pdf");
        FileInputStream fileInputStream = new FileInputStream(localFile);
        BaseRes<GetUploadUrlRes> res = docClient.getUploadFileUrl(new GetUploadUrlReq(FileTypeEnum.DOC.getCode()));

        docClient.upload(new BufferedInputStream(fileInputStream), res.getData().getUploadUrl());

        BaseRes<FileProcessRes> fileProcess = docClient.process(new FileProcessReq(List.of(new FddFileUrl(FileTypeEnum.DOC.getCode(), res.getData().getFddFileUrl(), "陈小菠-Java-广州.pdf"))));
        String fileId = fileProcess.getData().getFileIdList().iterator().next().getFileId();

        CreateSignTaskReq createSignTaskReq = new CreateSignTaskReq();

        createSignTaskReq.setInitiator(OpenId.getInstance(IdTypeEnum.CORP.getCode(), config.getOpenCorpId()));
        createSignTaskReq.setSignTaskSubject("合同签署");
        createSignTaskReq.setSignDocType("contract");
        createSignTaskReq.setAutoStart(true);

        AddDocInfo addDocInfo = new AddDocInfo();
        addDocInfo.setDocId("1");
        addDocInfo.setDocFileId(fileId);
        addDocInfo.setDocName("陈小菠-Java-广州");

        Field field = new Field();
        field.setFieldId("甲方签署字段");
        field.setFieldName("甲方签署");
        field.setFieldType("person_sign");

        FieldPosition position = new FieldPosition();
        position.setPositionMode("keyword");
        position.setPositionKeyword("陈小菠");

        field.setPosition(position);
        addDocInfo.setDocFields(List.of(field));

        AddActorsInfo addActorsInfo = new AddActorsInfo();

        Actor actor = new Actor();
        actor.setActorId("actorId");
        actor.setActorName("陈小菠");
        actor.setActorType("person");
        actor.setActorOpenId("296c747f69234a27be5d227b846d0ac6");
        actor.setPermissions(List.of("sign"));

        addActorsInfo.setActor(actor);
        AddSignFieldInfo addSignFieldInfo = new AddSignFieldInfo();
        addSignFieldInfo.setFieldDocId("1");
        addSignFieldInfo.setFieldId("甲方签署字段");
        addActorsInfo.setSignFields(List.of(addSignFieldInfo));

        createSignTaskReq.setActors(List.of(addActorsInfo));

        createSignTaskReq.setDocs(List.of(addDocInfo));

        BaseRes<CreateSignTaskRes> createSignTaskResBaseRes = signTaskClient.create(createSignTaskReq);
        System.out.println(createSignTaskResBaseRes.getData().getSignTaskId());
        BaseRes<GetSignTaskPreviewUrlRes> signTaskPreviewUrl = signTaskClient.getSignTaskPreviewUrl(new GetSignTaskUrlReq(createSignTaskResBaseRes.getData().getSignTaskId()));
        System.out.println(signTaskPreviewUrl.getData().getSignTaskPreviewUrl());
    }
}
