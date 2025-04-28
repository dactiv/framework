package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlRequestBody;
import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DocServiceTest {

    private final DocService docService;

    @Autowired
    public DocServiceTest(DocService docService) {
        this.docService = docService;
    }

    @Test
    public void testGetUploadUrl() {
        GetUploadUrlResponseBody body = docService.getUploadUrl(new GetUploadUrlRequestBody());
        Assertions.assertTrue(StringUtils.isNotEmpty(body.getUploadUrl()));
        Assertions.assertTrue(StringUtils.isNotEmpty(body.getFddFileUrl()));
    }


}
