package com.github.dactiv.framework.fasc.client;

import com.github.dactiv.framework.fasc.bean.base.BaseRes;
import com.github.dactiv.framework.fasc.constants.OpenApiUrlConstants;
import com.github.dactiv.framework.fasc.exception.ApiException;
import com.github.dactiv.framework.fasc.req.user.*;
import com.github.dactiv.framework.fasc.res.common.EUrlRes;
import com.github.dactiv.framework.fasc.res.user.GetChangeAccountRes;
import com.github.dactiv.framework.fasc.res.user.GetUserTransactionDetailRes;
import com.github.dactiv.framework.fasc.res.user.UserIdentityInfoRes;
import com.github.dactiv.framework.fasc.res.user.UserRes;

/**
 * @author Fadada
 * 2021/10/16 16:47:06
 */
public class UserClient {
    private final OpenApiClient openApiClient;

    public UserClient(OpenApiClient openApiClient) {
        this.openApiClient = openApiClient;
    }

    public BaseRes<Void> disable(DisableUserReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.USER_DISABLE, Void.class);
    }

    public BaseRes<Void> enable(EnableUserReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.USER_ENABLE, Void.class);
    }

    public BaseRes<UserRes> get(GetUserReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.USER_GET, UserRes.class);
    }

    public BaseRes<UserIdentityInfoRes> getIdentityInfo(GetUserIdentityInfoReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.USER_GET_IDENTITY_INFO, UserIdentityInfoRes.class);
    }

    public BaseRes<Void> unbind(UserUnbindReq req) throws ApiException{
        return openApiClient.invokeApi(req, OpenApiUrlConstants.USER_UNBIND, Void.class);
    }

    public BaseRes<EUrlRes> getUserAuthUrl(GetUserAuthUrlReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.USER_GET_AUTH_URL, EUrlRes.class);
    }

    public BaseRes<GetUserTransactionDetailRes> getUserIdentTransactionId(GetUserIdentTransactionIdReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.USER_GET_IDENT_TRANSACTION_ID, GetUserTransactionDetailRes.class);
    }

    public BaseRes<GetChangeAccountRes> getUserAccountChangeUrl(GetChangeAccountUrlReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.USER_GET_CHANGE_ACCOUNT_URL, GetChangeAccountRes.class);
    }
}
