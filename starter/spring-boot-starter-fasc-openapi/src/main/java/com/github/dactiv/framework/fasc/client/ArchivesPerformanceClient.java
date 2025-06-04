package com.github.dactiv.framework.fasc.client;

import com.github.dactiv.framework.fasc.bean.base.BaseRes;
import com.github.dactiv.framework.fasc.constants.OpenApiUrlConstants;
import com.github.dactiv.framework.fasc.exception.ApiException;
import com.github.dactiv.framework.fasc.req.archives.*;
import com.github.dactiv.framework.fasc.res.archives.*;

import java.util.List;

/**
 * @author zhoufucheng
 * @date 2023/11/8 14:01
 */
public class ArchivesPerformanceClient {
    private final OpenApiClient openApiClient;

    public ArchivesPerformanceClient(OpenApiClient openApiClient) {
        this.openApiClient = openApiClient;
    }

    //创建/修改履约
    public BaseRes<CreateOrModifyPerformanceRes> createOrModifyPerformance(CreateOrModifyPerformanceReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.ARCHIVES_PERFORMANCE_MODIFY, CreateOrModifyPerformanceRes.class);
    }

    //删除履约提醒
    public BaseRes<Void> deletePerformance(DeletePerformanceReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.ARCHIVES_PERFORMANCE_DELETE, Void.class);
    }

    //查询履约提醒
    public BaseRes<List<GetPerformanceListRes>> getPerformanceList(GetPerformanceListReq req) throws ApiException {
        return openApiClient.invokeApiList(req, OpenApiUrlConstants.ARCHIVES_PERFORMANCE_LIST, GetPerformanceListRes.class);
    }


    public BaseRes<ContactArchivedRes> contactArchived(ContactArchivedReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.CONTACT_ARCHIVED, ContactArchivedRes.class);
    }


    public BaseRes<ArchivedListRes> getArchivedList(ArchivedListReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.GET_ARCHIVED_LIST, ArchivedListRes.class);
    }


    public BaseRes<List<ArchivesCatalogListRes>> getArchivesCatalogList(ArchivesCatalogListReq req) throws ApiException {
        return openApiClient.invokeApiList(req, OpenApiUrlConstants.GET_ARCHIVES_CATALOG_LIST, ArchivesCatalogListRes.class);
    }


    public BaseRes<ArchivedDetailInfo> getArchivesDetail(ArchivesDetailReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.GET_ARCHIVES_DETAIL, ArchivedDetailInfo.class);
    }

    public BaseRes<GetArchivesManageUrlRes> getArchivesManageUrl(GetArchivesManageUrlReq req) throws ApiException {
        return openApiClient.invokeApi(req, OpenApiUrlConstants.GET_ARCHIVES_MANAGE_URL, GetArchivesManageUrlRes.class);
    }
}
