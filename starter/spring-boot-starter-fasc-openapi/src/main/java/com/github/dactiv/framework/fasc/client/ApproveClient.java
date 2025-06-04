package com.github.dactiv.framework.fasc.client;

import com.github.dactiv.framework.fasc.bean.base.BaseRes;
import com.github.dactiv.framework.fasc.constants.OpenApiUrlConstants;
import com.github.dactiv.framework.fasc.exception.ApiException;
import com.github.dactiv.framework.fasc.req.approval.*;
import com.github.dactiv.framework.fasc.res.approval.*;

import java.util.List;

/**
 * @author Fadada
 * 2023/06/14 16:48:09
 */
public class ApproveClient {

    private final OpenApiClient openApiClient;

    public ApproveClient(OpenApiClient openApiClient) {
        this.openApiClient = openApiClient;
    }


    public BaseRes<GetApprovalUrlRes> approvalGetUrl(GetApprovalUrlReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.APPROVAL_GET_URL, GetApprovalUrlRes.class);
    }

    /***/
    public BaseRes<List<GetApprovalFlowListRes>> approvalFlowGetList(GetApprovalFlowListReq req) throws ApiException {
        return openApiClient.invokeApiList(req, OpenApiUrlConstants.APPROVAL_FLOW_GET_LIST, GetApprovalFlowListRes.class);
    }

    /***/
    public BaseRes<GetApprovalFlowDetailRes> approvalFlowGetDetail(GetApprovalFlowDetailReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.APPROVAL_FLOW_GET_DETAIL, GetApprovalFlowDetailRes.class);
    }

    public BaseRes<GetApprovalInfoListRes> approvalGetList(GetApprovalInfoListReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.APPROVAL_GET_LIST, GetApprovalInfoListRes.class);
    }

    public BaseRes<GetApprovalDetailRes> approvalGetDetail(GetApprovalDetailReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.APPROVAL_GET_DETAIL, GetApprovalDetailRes.class);
    }


}
