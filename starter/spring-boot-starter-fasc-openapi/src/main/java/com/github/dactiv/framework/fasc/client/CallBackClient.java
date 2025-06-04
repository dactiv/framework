package com.github.dactiv.framework.fasc.client;

import com.github.dactiv.framework.fasc.bean.base.BaseRes;
import com.github.dactiv.framework.fasc.constants.OpenApiUrlConstants;
import com.github.dactiv.framework.fasc.exception.ApiException;
import com.github.dactiv.framework.fasc.req.callback.GetCallBackListReq;
import com.github.dactiv.framework.fasc.res.callback.GetCallBackListRes;

/**
 * @author zhoufucheng
 * @date 2023/6/14 14:10
 */
public class CallBackClient {
    private final OpenApiClient openApiClient;

    public CallBackClient(OpenApiClient openApiClient) {
        this.openApiClient = openApiClient;
    }

    public BaseRes<GetCallBackListRes> getCallBackList(GetCallBackListReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.GET_CALL_BACK_LIST, GetCallBackListRes.class);
    }
}
