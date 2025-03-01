package com.github.dactiv.framework.minio;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.minio.FileObject;
import com.github.dactiv.framework.minio.config.MinioProperties;
import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Part;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * 增强 minio 客户端
 *
 * @author maurice.chen
 */
public class EnhanceMinioClient extends MinioAsyncClient implements InitializingBean, SchedulingConfigurer {

    private final RestTemplate restTemplate;

    private final MinioProperties minioProperties;

    private Cookie cookie;

    protected EnhanceMinioClient(MinioAsyncClient client, MinioProperties minioProperties) {
        super(client);
        this.minioProperties = minioProperties;
        /*OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new OkHttpCookieJar())
                .build();*/

        //this.restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient));
        this.restTemplate = new RestTemplate();
    }

    /**
     * 创建分片上传请求
     *
     * @param fileObject       文件对象
     * @param headers          消息头
     * @param extraQueryParams 额外查询参数
     *
     * @return 创建分片上传响应体
     */
    public CreateMultipartUploadResponse createMultipartUpload(FileObject fileObject, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        return super.createMultipartUploadAsync(fileObject.getBucketName(), fileObject.getRegion(), fileObject.getObjectName(), headers, extraQueryParams).get();
    }

    /**
     * 完成分片上传，执行合并文件
     *
     * @param fileObject       文件对象
     * @param uploadId         上传ID
     * @param parts            分片
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     *
     * @return 文件对象创建情况响应体
     */
    public ObjectWriteResponse completeMultipartUpload(FileObject fileObject, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        return super.completeMultipartUploadAsync(fileObject.getBucketName(), fileObject.getRegion(), fileObject.getObjectName(), uploadId, parts, extraHeaders, extraQueryParams).get();
    }

    /**
     * 针对文件对象查询文件分片内容
     *
     * @param fileObject 文件对象
     * @param maxParts 文件部分内容的最大值
     * @param partNumberMarker 文件部分内容位置编号
     * @param uploadId 上传 id
     * @param extraHeaders 额外消息头
     * @param extraQueryParams 额外查询参数
     *
     * @return 文件分片内容响应体
     */
    public ListPartsResponse listParts(FileObject fileObject, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        return super.listPartsAsync(fileObject.getBucketName(), fileObject.getRegion(), fileObject.getObjectName(), maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams).get();
    }

    /**
     * 获取桶信息
     *
     * @param name 桶名称，如果不未 “” 或者 null，讲返回 api buckets 的所有内容，否则返回 api buckets 接口的 buckets 单个对象信息
     *
     * @return 桶信息
     */
    public Map<String, Object> buckets(String name) {
        Map<String, Object> result = exchangeConsoleApi("buckets", HttpMethod.GET, new LinkedHashMap<>());
        if (StringUtils.isNotEmpty(name)) {
            List<Map<String, Object>> buckets = Casts.cast(result.get("buckets"));
            return buckets.stream().filter(m -> name.equals(m.get("name"))).findFirst().orElse(null);
        }

        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setCookieValue();
    }

    private void refreshCookie() {
        if (cookie.expiresAt() + minioProperties.getCookieMinRemainingBeforeRefresh().toMillis() < System.currentTimeMillis()) {
            return ;
        }

        setCookieValue();
    }

    private void setCookieValue() {
        if (StringUtils.isEmpty(minioProperties.getEndpoint())) {
            return ;
        }

        Map<String, Object> param = Map.of(
                "accessKey", minioProperties.getAccessKey(),
                "secretKey", minioProperties.getSecretKey()
        );
        ResponseEntity<Map<String, Object>> result = restTemplate.exchange(
                minioProperties.getConsoleApiAddress("login"),
                HttpMethod.POST,
                new HttpEntity<>(param),
                new ParameterizedTypeReference<>() { });

        Assert.isTrue(
                result.getStatusCode().is2xxSuccessful(),
                HttpStatus.valueOf(result.getStatusCode().value()).getReasonPhrase()
        );

        String cookieValue = result.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        Assert.isTrue(StringUtils.isNotEmpty(cookieValue), "header " + HttpHeaders.SET_COOKIE + " is empty");

        cookie = Cookie.parse(HttpUrl.parse(minioProperties.getEndpoint()), cookieValue);
    }

    public <T> T exchangeConsoleApi(String apiName, HttpMethod method, Map<String, Object> body) {
        Assert.notNull(cookie, "cookie is null");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE,cookie.name() + Casts.EQ + cookie.value());
        ResponseEntity<T> result = restTemplate.exchange(
                minioProperties.getConsoleApiAddress(apiName),
                method,
                new HttpEntity<>(body, httpHeaders),
                new ParameterizedTypeReference<>() { }
        );

        Assert.isTrue(
                result.getStatusCode().is2xxSuccessful(),
                HttpStatus.valueOf(result.getStatusCode().value()).getReasonPhrase()
        );

        return result.getBody();

    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // FIXME 怎么做成可动态的配置
        taskRegistrar.addCronTask(this::refreshCookie, minioProperties.getRefreshCookieCron());
    }
}
