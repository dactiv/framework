package com.github.dactiv.framework.fasc.client;

import com.github.dactiv.framework.fasc.bean.base.BaseRes;
import com.github.dactiv.framework.fasc.constants.OpenApiUrlConstants;
import com.github.dactiv.framework.fasc.exception.ApiException;
import com.github.dactiv.framework.fasc.req.eui.GetAppPageResourceUrlReq;
import com.github.dactiv.framework.fasc.req.eui.GetBillUrlReq;
import com.github.dactiv.framework.fasc.req.eui.GetUserPageResourceUrlReq;
import com.github.dactiv.framework.fasc.res.common.EUrlRes;
import com.github.dactiv.framework.fasc.res.eui.GetPageResourceUrlRes;

public class EUIClient {
    private final OpenApiClient openApiClient;

    public EUIClient(OpenApiClient openApiClient) {
        this.openApiClient = openApiClient;
    }

    public BaseRes<EUrlRes> getBillUrl(GetBillUrlReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.BILLING_GET_BILL_URL, EUrlRes.class);
    }

    public BaseRes<GetPageResourceUrlRes> getAppPageResourceUrl(GetAppPageResourceUrlReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.APP_PAGE_RESOURCE_GET_URL, GetPageResourceUrlRes.class);
    }

    public BaseRes<GetPageResourceUrlRes> getUserPageResourceUrl(GetUserPageResourceUrlReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.USER_PAGE_RESOURCE_GET_URL, GetPageResourceUrlRes.class);
    }
}
