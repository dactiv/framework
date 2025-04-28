package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlRequestBody;
import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlResponseBody;
import com.github.dactiv.framework.fadada.domain.body.task.CreateSignTaskRequestBody;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FadadaServiceTest {

    private final FadadaService fadadaService;

    @Autowired
    public FadadaServiceTest(FadadaService fadadaService) {
        this.fadadaService = fadadaService;
    }

    @Test
    public void testGetUploadUrl() {
        GetUploadUrlResponseBody body = fadadaService.getUploadUrl(new GetUploadUrlRequestBody());
        Assertions.assertTrue(StringUtils.isNotEmpty(body.getUploadUrl()));
        Assertions.assertTrue(StringUtils.isNotEmpty(body.getFddFileUrl()));
    }

    @Test
    public void testCreateSignTaskWithTemplate() {
        CreateSignTaskRequestBody body = new CreateSignTaskRequestBody();
        body.setSignTaskSubject("签署任务模板发起V5.1签署任务-个人1677138236");
    }
}
