package com.github.dactiv.framework.fadada.service;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.fadada.config.FadadaConfig;
import com.github.dactiv.framework.fadada.domain.body.doc.FileProcessRequestBody;
import com.github.dactiv.framework.fadada.domain.body.doc.FileProcessResponseBody;
import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlRequestBody;
import com.github.dactiv.framework.fadada.domain.body.doc.GetUploadUrlResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class DocService extends FadadaBasicService {

    private final AuthService authService;

    public DocService(FadadaConfig fadadaConfig, RestTemplate restTemplate, AuthService authService) {
        super(fadadaConfig, restTemplate);
        this.authService = authService;
    }

    public GetUploadUrlResponseBody getUploadUrl(GetUploadUrlRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/file/get-upload-url", authService.getAccessTokenIfCacheNull().getToken(), param, GetUploadUrlResponseBody.class);
    }

    public FileProcessResponseBody processFile(FileProcessRequestBody body) {
        Map<String, Object> param = Casts.convertValue(body, Casts.MAP_TYPE_REFERENCE);
        return executeApi("/file/process", authService.getAccessTokenIfCacheNull().getToken(), param, FileProcessResponseBody.class);
    }

    public RestResult<?> upload(BufferedInputStream inputStream, String uploadUrl) throws Exception {

        URL url = new URL(uploadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setDoOutput(true);
        BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
        int readByte;
        while ((readByte = inputStream.read()) != -1) {
            bos.write(readByte);
        }
        inputStream.close();
        bos.close();

        return RestResult.of(connection.getResponseMessage(),connection.getResponseCode(), String.valueOf(connection.getResponseCode()));
    }

}
