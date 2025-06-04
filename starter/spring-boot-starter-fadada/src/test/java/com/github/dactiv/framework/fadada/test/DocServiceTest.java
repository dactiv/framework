package com.github.dactiv.framework.fadada.test;

import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.fadada.domain.body.doc.FileProcessRequestBody;
import com.github.dactiv.framework.fadada.domain.body.doc.FileProcessResponseBody;
import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlRequestBody;
import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlResponseBody;
import com.github.dactiv.framework.fadada.domain.metadata.doc.FddFileUrlMetadata;
import com.github.dactiv.framework.fadada.enumerate.DocFileType;
import com.github.dactiv.framework.fadada.service.DocService;
import com.github.dactiv.framework.fadada.service.SignTaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@SpringBootTest
public class DocServiceTest {

    @Autowired
    private DocService docService;

    private SignTaskService signTaskService;

    @Test
    public void testUpload() throws Exception {
        GetUploadUrlResponseBody responseBody = docService.getUploadUrl(new GetUploadUrlRequestBody());
        File localFile = new File("C:/Users/olale/OneDrive/文档/陈小菠-Java-广州.pdf");
        FileInputStream fileInputStream = new FileInputStream(localFile);
        RestResult<?> result = docService.upload(new BufferedInputStream(fileInputStream),responseBody.getUploadUrl());
        Assertions.assertTrue(HttpStatus.OK.value() == result.getStatus());
        FileProcessResponseBody fileProcessResponseBody = docService.processFile(new FileProcessRequestBody(List.of(new FddFileUrlMetadata(DocFileType.DOC.getValue(), responseBody.getFddFileUrl(), "陈小菠-Java-广州.pdf"))));

        String fileId = fileProcessResponseBody.getFileIdList().iterator().next().getFileId();
        //signTaskService.createSignTaskWithTemplate()
    }
}
