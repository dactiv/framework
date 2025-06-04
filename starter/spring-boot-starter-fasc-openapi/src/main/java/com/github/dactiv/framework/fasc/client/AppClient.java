package com.github.dactiv.framework.fasc.client;

import com.github.dactiv.framework.fasc.bean.base.BaseRes;
import com.github.dactiv.framework.fasc.constants.OpenApiUrlConstants;
import com.github.dactiv.framework.fasc.exception.ApiException;
import com.github.dactiv.framework.fasc.req.app.GetAppOpenIdListReq;
import com.github.dactiv.framework.fasc.res.app.GetAppOpenIdListRes;

/**
 * @author zhoufucheng
 * @date 2023/12/19 14:42
 */
public class AppClient {
    private final OpenApiClient openApiClient;

    public AppClient(OpenApiClient openApiClient) {
        this.openApiClient = openApiClient;
    }

    public BaseRes<GetAppOpenIdListRes> getOpenIdList(GetAppOpenIdListReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.GET_OPEN_ID_LIST, GetAppOpenIdListRes.class);
    }
}
