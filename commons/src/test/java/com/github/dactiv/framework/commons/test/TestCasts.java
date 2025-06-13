package com.github.dactiv.framework.commons.test;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.domain.metadata.TreeDescriptionMetadata;
import com.github.dactiv.framework.commons.test.enitty.DescriptionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestCasts {

    @Test
    public void testSetUrlPathVariableValue() {
        String url = "http://localhost:9010/api/v1/buckets/{bucketName}/objects/download?prefix={filename}";
        Map<String, String> variableValue = new LinkedHashMap<>();
        variableValue.put("bucketName", "email");
        variableValue.put("filename", "test.json");
        String value = Casts.setUrlPathVariableValue(url, variableValue);

        Assertions.assertEquals(value, "http://localhost:9010/api/v1/buckets/email/objects/download?prefix=test.json");

        variableValue.clear();

        variableValue.put("bucketName", "email");
        value = Casts.setUrlPathVariableValue(url, variableValue);

        Assertions.assertEquals(value, "http://localhost:9010/api/v1/buckets/email/objects/download?prefix={filename}");

        variableValue.clear();

        value = Casts.setUrlPathVariableValue(url, variableValue);

        Assertions.assertEquals(value, "http://localhost:9010/api/v1/buckets/{bucketName}/objects/download?prefix={filename}");

        String json = "{\"templateCode\":\"SMS_314856207\",\"signCode\":\"广西海象爱家科技\"}";
    }

    @Test
    public void testConvert(){
        TreeDescriptionMetadata metadata = Casts.convertDescriptionMetadata(DescriptionEntity.class);
    }
}
