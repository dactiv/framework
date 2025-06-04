package com.github.dactiv.framework.fasc.client;

import com.github.dactiv.framework.fasc.bean.base.BaseRes;
import com.github.dactiv.framework.fasc.constants.OpenApiUrlConstants;
import com.github.dactiv.framework.fasc.exception.ApiException;
import com.github.dactiv.framework.fasc.req.doc.*;
import com.github.dactiv.framework.fasc.res.doc.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author Fadada
 * 2021/9/8 16:09:38
 */
public class DocClient {
    private final OpenApiClient openApiClient;

    public DocClient(OpenApiClient openApiClient) {
        this.openApiClient = openApiClient;
    }

    public BaseRes<UploadFileByUrlRes> uploadFileByUrl(UploadFileByUrlReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.FILE_UPLOAD_BY_URL, UploadFileByUrlRes.class);
    }

    public BaseRes<GetUploadUrlRes> getUploadFileUrl(GetUploadUrlReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.FILE_GET_UPLOAD_URL, GetUploadUrlRes.class);
    }

    public BaseRes<FileProcessRes> process(FileProcessReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.FILE_PROCESS, FileProcessRes.class);
    }
    /** 文档验签 **/
    public BaseRes<FileVerifySignRes> fileVerifySign(FileVerifySignReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.DOC_POST_FILE_VERIFY_SIGN, FileVerifySignRes.class);
    }

    public BaseRes<List<GetKeywordPositionRes>> getKeywordPosition(GetKeywordPositionReq req) throws ApiException {
        return openApiClient.invokeApiList(req, OpenApiUrlConstants.GET_KEYWORD_POSITIONS, GetKeywordPositionRes.class);
    }

    public BaseRes<OfdFileMergeRes> ofdFileMerge(OfdFileMergeReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.OFD_FILE_MERGE, OfdFileMergeRes.class);
    }

    public void upload(BufferedInputStream inputStream, String uploadUrl) throws Exception {

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
    }
}
