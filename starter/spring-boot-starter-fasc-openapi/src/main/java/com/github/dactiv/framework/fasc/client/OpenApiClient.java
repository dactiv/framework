package com.github.dactiv.framework.fasc.client;

import com.github.dactiv.framework.commons.TimeProperties;
import com.github.dactiv.framework.commons.annotation.Time;
import com.github.dactiv.framework.commons.domain.AccessToken;
import com.github.dactiv.framework.commons.domain.metadata.RefreshAccessTokenMetadata;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.fasc.bean.base.BaseReq;
import com.github.dactiv.framework.fasc.bean.base.BaseRes;
import com.github.dactiv.framework.fasc.bean.base.BaseResponseEntity;
import com.github.dactiv.framework.fasc.bean.base.HttpInfoRes;
import com.github.dactiv.framework.fasc.config.FascConfig;
import com.github.dactiv.framework.fasc.constants.OpenApiUrlConstants;
import com.github.dactiv.framework.fasc.constants.RequestConstants;
import com.github.dactiv.framework.fasc.exception.ApiException;
import com.github.dactiv.framework.fasc.res.service.AccessTokenRes;
import com.github.dactiv.framework.fasc.utils.crypt.FddCryptUtil;
import com.github.dactiv.framework.fasc.utils.http.HttpUtil;
import com.github.dactiv.framework.fasc.utils.json.ParameterizedTypeBaseRes;
import com.github.dactiv.framework.fasc.utils.random.UUIDGenerator;
import com.github.dactiv.framework.idempotent.ConcurrentConfig;
import com.github.dactiv.framework.idempotent.advisor.concurrent.ConcurrentInterceptor;
import com.github.dactiv.framework.idempotent.annotation.Concurrent;
import com.github.dactiv.framework.nacos.task.annotation.NacosCronScheduled;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Fadada
 * 2021/9/8 16:09:38
 */

public class OpenApiClient implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(OpenApiClient.class);

    private final FascConfig config;

    private final ConcurrentInterceptor concurrentInterceptor;

    public OpenApiClient(FascConfig config, ConcurrentInterceptor concurrentInterceptor) {
        this.config = config;
        this.concurrentInterceptor = concurrentInterceptor;
    }

    public FascConfig getConfig() {
        return config;
    }

    /**
     * 根据请求返回实体类
     *
     * @param req  请求实体
     * @param path 请求路径
     * @param clzz 指定响应实体类
     * @param <T>  响应实体泛型
     * @return BaseRsp
     * @throws ApiException API异常
     */
    <T> BaseRes<T> invokeApi(BaseReq req, String path, Class<T> clzz) throws ApiException {
        return this.httpRequest(req, path, null, clzz);
    }


    /**
     * 根据请求返回实体类
     *
     * @param req  请求实体
     * @param path 请求路径
     * @param clzz 指定响应实体类
     * @param <T>  响应实体泛型
     * @return BaseRsp
     * @throws ApiException API异常
     */
    <T> BaseRes<List<T>> invokeApiList(BaseReq req, String path, Class<T> clzz) throws ApiException {
        return this.httpRequestList(req, path, clzz);
    }


    /**
     * 上传文件返回实体类
     *
     * @param req   请求实体
     * @param path  请求路径
     * @param files 请求文件
     * @param clzz  指定响应实体类
     * @param <T>   响应实体泛型
     * @return BaseRsp
     * @throws ApiException API异常
     */
    <T> BaseRes<T> invokeApi(BaseReq req, String path, Map<String, File> files, Class<T> clzz) throws ApiException {
        return httpRequest(req, path, files, clzz);
    }

    /**
     * 下载返回对应实体类
     *
     * @param req  请求实体
     * @param path 请求路径
     * @return BaseRes
     * @throws ApiException API异常
     */
    BaseRes<BaseResponseEntity> invokeApiDownload(BaseReq req, String path) throws ApiException {
        String accessToken = getAccessTokenIfNullRefresh().getToken();
        Map<String, String> bodyMap = getBodyMap(req);
        Map<String, String> headerMap = getSign(getHeaderMap(accessToken), bodyMap);
        String method = getMethod(path);
        path = path.replace(method, StringUtils.EMPTY).trim();
        String url = config.getServerUrl() + path;

        BaseResponseEntity baseResponseEntity = request(url, headerMap, bodyMap);
        BaseRes<BaseResponseEntity> res;

        if (baseResponseEntity.getData() != null) {
            res = config.getJsonStrategy().toJavaBean(baseResponseEntity.getData(), BaseRes.class);
        } else {
            res = new BaseRes();
            res.setData(baseResponseEntity);
        }
        res.setHttpStatusCode(baseResponseEntity.getHttpStatusCode());
        return res;
    }

    private <T> BaseRes<List<T>> httpRequestList(BaseReq req, String path, Class<T> clzz)
            throws ApiException {
        HttpInfoRes httpInfoRes = this.httpRequest(req, path, null);
        if (httpInfoRes == null) {
            return null;
        }

        BaseRes baseRes = config.getJsonStrategy().toJavaBean(httpInfoRes.getBody(), BaseRes.class);
        baseRes.setHttpStatusCode(httpInfoRes.getHttpStatusCode());
        Object data = baseRes.getData();
        if (data != null) {
            String jsonStr;
            if (data instanceof String) {
                jsonStr = String.valueOf(data);
            } else {
                jsonStr = config.getJsonStrategy().toJson(data);
            }
            List<T> lists = config.getJsonStrategy().toList(jsonStr, clzz);
            baseRes.setData(lists);
        }
        return baseRes;
    }

    private <T> BaseRes<T> httpRequest(BaseReq req, String path, Map<String, File> files, Class<T> clzz)
            throws ApiException {
        HttpInfoRes httpInfoRes = this.httpRequest(req, path, files);
        if (httpInfoRes == null) {
            return null;
        }
        BaseRes<T> baseRes = config.getJsonStrategy().toJavaBean(httpInfoRes.getBody(), new ParameterizedTypeBaseRes(clzz));
        baseRes.setHttpStatusCode(httpInfoRes.getHttpStatusCode());
        SystemException.isTrue(config.getSuccessCodeValue().equals(baseRes.getCode()), baseRes.getMsg());
        return baseRes;
    }

    private HttpInfoRes httpRequest(BaseReq req, String path, Map<String, File> files) throws ApiException {
        String accessToken = Objects.isNull(req) ? null : Objects.toString(req.getAccessToken(), getAccessTokenIfNullRefresh().getToken());
        HashMap<String, String> bodyMap = getBodyMap(req);
        Map<String, String> headerMap = getSign(getHeaderMap(accessToken), bodyMap);
        String method = getMethod(path);
        path = path.replace(method, StringUtils.EMPTY).trim();
        String url = config.getServerUrl() + path;
        return request(url, method, headerMap, bodyMap, files);
    }

    private String getMethod(String path) {
        String method;
        if (path.startsWith(RequestConstants.METHOD_POST)) {
            method = RequestConstants.METHOD_POST;
        } else if (path.startsWith(RequestConstants.METHOD_GET)) {
            method = RequestConstants.METHOD_GET;
        } else {
            throw new IllegalArgumentException("path值非法");
        }
        return method;
    }


    private Map<String, String> getSign(Map<String, String> headerMap, Map<String, String> bodyMap) {
        try {
            Map<String, String> signMap = new HashMap<>(headerMap);
            if (null != bodyMap) {
                signMap.putAll(bodyMap);
            }
            String sortParam = FddCryptUtil.sortParameters(signMap);
            String sign = FddCryptUtil.sign(sortParam, headerMap.get(RequestConstants.TIMESTAMP), config.getAccessToken().getSecretKey());
            headerMap.put(RequestConstants.SIGN, sign);
        } catch (Exception e) {
            log.error("计算签名失败：{}", e.getMessage(), e);
        }
        return headerMap;
    }


    private HashMap<String, String> getHeaderMap(String accessToken) {
        HashMap<String, String> paraMap = new HashMap<>();
        paraMap.put(RequestConstants.APP_ID, config.getAccessToken().getSecretId());
        paraMap.put(RequestConstants.API_SUB_VERSION, RequestConstants.CURRENT_SUB_VERSION);
        paraMap.put(RequestConstants.SIGN_TYPE, config.getSignType());
        if (accessToken != null) {
            paraMap.put(RequestConstants.ACCESS_TOKEN, accessToken);
            paraMap.remove(RequestConstants.DATA_KEY);
        } else {
            paraMap.put(RequestConstants.GRANT_TYPE, RequestConstants.CLIENT_CREDENTIAL);
        }

        paraMap.put(RequestConstants.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        paraMap.put(RequestConstants.NONCE, UUIDGenerator.getUuid());
        return paraMap;


    }

    private HashMap<String, String> getBodyMap(BaseReq req) throws ApiException {
        if (req == null) {
            return new HashMap<>();
        }
        String bizContent;
        req.setAccessToken(null);
        bizContent = config.getJsonStrategy().toJson(req);

        HashMap<String, String> bodyMap = new HashMap<>(1);
        bodyMap.put(RequestConstants.DATA_KEY, bizContent);
        return bodyMap;
    }


    private HttpInfoRes request(String url, String method, Map<String, String> headerMap, Map<String, String> bodyMap, Map<String, File> fileMap) throws ApiException {
        try {
            if (RequestConstants.METHOD_GET.equals(method)) {
                return HttpUtil.get(url, headerMap, bodyMap);
            } else if (RequestConstants.METHOD_POST.equals(method)) {
                return HttpUtil.post(url, headerMap, bodyMap, fileMap);
            }
            return null;
        } catch (ApiException apie) {
            throw apie;
        } catch (Exception e) {
            log.error("http请求失败：{}", e.getMessage(), e);
            throw new ApiException("http请求失败");
        }
    }


    private BaseResponseEntity request(String url, Map<String, String> headerMap, Map<String, String> bodyMap) throws ApiException {
        try {
            return HttpUtil.downLoadFiles(url, headerMap, bodyMap);
        } catch (Exception e) {
            log.error("httpDownLoad请求失败：{}", e.getMessage(), e);
            throw new ApiException("httpDownLoad请求失败");
        }
    }

    @Async
    @Override
    public void afterPropertiesSet() {
        try {
            AccessToken accessToken = getAccessTokenIfNullRefresh();
            log.info("[法大大] 当前 token 为: {}, 在: {} 后超时", accessToken.getToken(), accessToken.getExpiresInDateTime());
        } catch (Exception e) {
            log.error("[法大大] 获取访问 token 失败", e);
        }
    }

    public AccessToken getAccessTokenIfNullRefresh() {
        AccessToken accessToken = getCacheAccessToken();
        if (Objects.isNull(accessToken)) {
            accessToken = refreshAccessToken();
        }
        return accessToken;
    }

    public AccessToken getAccessToken() throws ApiException {
        BaseRes<AccessTokenRes> res = invokeApi(null, OpenApiUrlConstants.SERVICE_GET_ACCESS_TOKEN, AccessTokenRes.class);
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(res.getData().getAccessToken());
        accessToken.setExpiresTime(TimeProperties.of(Long.parseLong(res.getData().getExpiresIn()), TimeUnit.SECONDS));
        return accessToken;
    }

    @NacosCronScheduled(cron = "${dactiv.fasc.refresh-access-token-cron:0 0/30 * * * ? }")
    @Concurrent(value = "dactiv:fasc:refresh-access-token:concurrent" , waitTime = @Time(value = 8, unit = TimeUnit.SECONDS), leaseTime = @Time(value = 5, unit = TimeUnit.SECONDS), exception = "刷新法大大 accessToken 出现并发")
    public AccessToken refreshAccessToken() {
        AccessToken token = getCacheAccessToken();
        RefreshAccessTokenMetadata refreshAccessToken = getConfig().getAccessToken();
        if (Objects.nonNull(token) && System.currentTimeMillis() - token.getCreationTime().getTime() > refreshAccessToken.getRefreshAccessTokenLeadTime().toMillis()) {
            return token;
        }

        ConcurrentConfig config = new ConcurrentConfig();

        config.setKey(getConfig().getAccessToken().getCache().getName(":concurrent"));
        config.setException("获取发大大 accessToken 出现并发");
        config.setWaitTime(TimeProperties.of(8, TimeUnit.SECONDS));
        config.setLeaseTime(TimeProperties.of(5, TimeUnit.SECONDS));

        token = concurrentInterceptor.invoke(config, () -> SystemException.convertSupplier(this::getAccessToken));
        RBucket<AccessToken> bucket = concurrentInterceptor
                .getRedissonClient()
                .getBucket(getConfig().getAccessToken().getCache().getName());
        bucket.set(token);
        bucket.expire(token.getExpiresTime().toDuration());

        return token;
    }

    private AccessToken getCacheAccessToken(){
        RBucket<AccessToken> bucket = concurrentInterceptor
                .getRedissonClient()
                .getBucket(getConfig().getAccessToken().getCache().getName());
        return bucket.get();
    }
}
