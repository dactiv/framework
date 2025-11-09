package com.github.dactiv.framework.minio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.commons.minio.Bucket;
import com.github.dactiv.framework.commons.minio.FileObject;
import com.github.dactiv.framework.commons.minio.MoveFileObject;
import com.github.dactiv.framework.commons.minio.VersionFileObject;
import com.github.dactiv.framework.minio.config.MinioProperties;
import com.google.common.collect.Multimaps;
import io.minio.*;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import io.minio.messages.Part;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * minio 异步操作类库
 *
 * @author maurice.chen
 */
public class MinioAsyncTemplate extends ConsoleApiMinioAsyncClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(MinioAsyncTemplate.class);

    private static final int PART_SIZE = 5 * 1024 * 1024;

    /**
     * 分片参数参数
     */
    public static final String PART_NUMBER_PARAM_NAME = "partNumber";

    /**
     * 分片数量参数
     */
    public static final String CHUNK_PARAM_NAME = "chunk";

    /**
     * 上传 id
     */
    public static final String UPLOAD_ID_PARAM_NAME = "uploadId";

    /**
     * 上传者 id
     */
    public static final String UPLOADER_ID = "uploader-id";

    /**
     * AMZ 元数据上传者 id
     */
    public static final String AMZ_META_UPLOADER_ID = Bucket.AMZ_META_PREFIX + UPLOADER_ID;

    /**
     * 用户元数据信息
     */
    public static final String USER_METADATA = "userMetadata";

    private final Executor ioExecutor;

    private final ObjectMapper objectMapper;

    protected MinioAsyncTemplate(MinioAsyncClient client,
                                 ObjectMapper objectMapper,
                                 MinioProperties minioProperties) {
        super(client, minioProperties);
        ioExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        this.objectMapper = objectMapper;
    }

    /**
     * 创建桶, 如果桶不存在就创建，否则不创建。
     *
     * @param bucket 桶描述
     * @return 如果桶存在返回 false，否则创建桶后返回 true
     */
    public CompletableFuture<Boolean> makeBucketIfNotExists(Bucket bucket) {
        return isBucketExist(bucket).thenCompose(exist -> afterMakeBucketIfNotExists(bucket, exist));
    }

    private CompletableFuture<Boolean> afterMakeBucketIfNotExists(Bucket bucket, boolean exist) {
        if (exist) {
            return CompletableFuture.completedFuture(false);
        }

        MakeBucketArgs args = MakeBucketArgs
                .builder()
                .bucket(bucket.getBucketName().toLowerCase())
                .region(bucket.getRegion())
                .extraHeaders(bucket.getExtraHeaders())
                .extraQueryParams(bucket.getExtraQueryParams())
                .build();
        return SystemException.convertSupplier(
                () -> makeBucket(args).thenApply(v -> true),
                "[minio async template] makeBucket error"
        );
    }

    /**
     * 判断桶是否存在
     *
     * @param bucket 桶描述
     * @return tru 存在，否则 false
     */
    public CompletableFuture<Boolean> isBucketExist(Bucket bucket) {

        BucketArgs.Builder<BucketExistsArgs.Builder, BucketExistsArgs> builder = BucketExistsArgs
                .builder()
                .bucket(bucket.getBucketName())
                .extraHeaders(bucket.getExtraHeaders())
                .extraQueryParams(bucket.getExtraQueryParams());
        if (StringUtils.isNotEmpty(bucket.getRegion())) {
            builder.region(bucket.getRegion());
        }

        return SystemException.convertSupplier(
                () -> bucketExists(builder.build()),
                "[minio async template] bucketExists error"
        );
    }

    /**
     * 删除文件
     *
     * @param fileObject          文件对象描述
     * @param deleteBucketIfEmpty true: 如果桶的文件目录为空，删除桶，否则 false
     */
    public CompletableFuture<Boolean> deleteObject(FileObject fileObject, boolean deleteBucketIfEmpty) {

        String bucketName = fileObject.getBucketName().toLowerCase();

        if (StringUtils.endsWith(fileObject.getObjectName(), AntPathMatcher.DEFAULT_PATH_SEPARATOR) ||
                StringUtils.endsWith(fileObject.getObjectName(), Casts.DOT)) {

            return CompletableFuture
                    .supplyAsync(() -> createDeleteObjectStream(fileObject), ioExecutor)
                    .thenApply(objects -> RemoveObjectsArgs
                            .builder()
                            .bucket(fileObject.getBucketName())
                            .region(fileObject.getRegion())
                            .objects(objects)
                            .extraHeaders(fileObject.getExtraHeaders())
                            .extraQueryParams(fileObject.getExtraQueryParams())
                            .build())
                    .thenApplyAsync(this::removeObjects, ioExecutor)
                    .thenCompose(removeObjectsResult ->
                            CompletableFuture
                                    .completedFuture(
                                            StreamSupport
                                                    .stream(removeObjectsResult.spliterator(), false)
                                                    .map(r -> SystemException.convertSupplier(r::get, "[minio async template] get DeleteError error"))
                                                    .toList()
                                    )
                    )
                    .thenCompose(v -> afterDeleteObject(fileObject, deleteBucketIfEmpty));
        } else {

            RemoveObjectArgs.Builder args = RemoveObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .region(fileObject.getRegion())
                    .object(fileObject.getObjectName())
                    .extraHeaders(fileObject.getExtraHeaders())
                    .extraQueryParams(fileObject.getExtraQueryParams());

            if (VersionFileObject.class.isAssignableFrom(fileObject.getClass())) {
                VersionFileObject version = Casts.cast(fileObject);
                args.versionId(version.getVersionId());
            }
            try {
                statObject(StatObjectArgs.builder().bucket(bucketName).object(fileObject.getObjectName()).build());
                return SystemException.convertSupplier(
                        () -> removeObject(args.build()).thenCompose(v -> afterDeleteObject(fileObject, deleteBucketIfEmpty)),
                        "[minio async template] removeBucket error"
                );
            } catch (Exception e) {
                LOGGER.warn("[minio async template] removeBucket error", e);
                return CompletableFuture.completedFuture(Boolean.FALSE);
            }
        }

    }

    private List<DeleteObject> createDeleteObjectStream(FileObject fileObject) {
        ListObjectsArgs args = ListObjectsArgs
                .builder()
                .bucket(fileObject.getBucketName())
                .region(fileObject.getRegion())
                .prefix(fileObject.getObjectName())
                .extraHeaders(fileObject.getExtraHeaders())
                .extraQueryParams(fileObject.getExtraQueryParams())
                .build();
        return StreamSupport
                .stream(listObjects(args).spliterator(), false)
                .map(r -> SystemException.convertSupplier(r::get, "[minio async template] get item error"))
                .filter(Objects::nonNull)
                .map(item -> new DeleteObject(item.objectName()))
                .collect(Collectors.toList());
    }

    private CompletableFuture<Boolean> afterDeleteObject(FileObject fileObject, boolean deleteBucketIfEmpty) {
        if (!deleteBucketIfEmpty) {
            return CompletableFuture.completedFuture(false);
        }

        ListObjectsArgs listObjectsArgs = ListObjectsArgs
                .builder()
                .bucket(fileObject.getBucketName())
                .region(fileObject.getRegion())
                .extraHeaders(fileObject.getExtraHeaders())
                .extraQueryParams(fileObject.getExtraQueryParams())
                .build();
        Iterable<Result<Item>> iterable = listObjects(listObjectsArgs);

        if (!iterable.iterator().hasNext()) {
            Bucket bucket = Bucket.of(fileObject.getBucketName(), fileObject.getRegion());
            return deleteBucket(bucket);
        }

        return CompletableFuture.completedFuture(false);
    }

    /**
     * 删除桶
     *
     * @param bucket 桶描述
     */
    public CompletableFuture<Boolean> deleteBucket(Bucket bucket) {

        return SystemException.convertSupplier(
                () -> isBucketExist(bucket).thenCompose(exist -> doDeleteBucketIfEmpty(exist, bucket)),
                "[minio async template] isBucketExist error"
        );

    }

    private CompletableFuture<Boolean> doDeleteBucketIfEmpty(Boolean exist, Bucket bucket) {
        if (!exist) {
            return CompletableFuture.completedFuture(false);
        }
        RemoveBucketArgs removeBucketArgs = RemoveBucketArgs
                .builder()
                .bucket(bucket.getBucketName())
                .region(bucket.getRegion())
                .extraHeaders(bucket.getExtraHeaders())
                .extraQueryParams(bucket.getExtraQueryParams())
                .build();
        return SystemException.convertSupplier(
                () -> removeBucket(removeBucketArgs).thenApply(v -> true),
                "[minio async template] removeBucket error"
        );

    }

    /**
     * 移动文件
     *
     * @param object 对懂文件对象
     * @return minio API 调用响应的 ObjectWriteResponse 对象
     */
    public CompletableFuture<ObjectWriteResponse> moveObject(MoveFileObject object) {
        return copyObject(object.getSource(), object.getTarget())
                .thenCompose(v -> deleteObject(object.getSource(), object.isDeleteBucketIfEmpty()).thenApply(vo -> v));
    }

    /**
     * 获取文件
     *
     * @return 输入流
     */
    public CompletableFuture<GetObjectResponse> getObject(FileObject fileObject) {
        ObjectVersionArgs.Builder<GetObjectArgs.Builder, GetObjectArgs> getObjectArgs = GetObjectArgs
                .builder()
                .bucket(fileObject.getBucketName().toLowerCase())
                .region(fileObject.getRegion())
                .object(fileObject.getObjectName())
                .extraHeaders(fileObject.getExtraHeaders())
                .extraQueryParams(fileObject.getExtraQueryParams());

        if (VersionFileObject.class.isAssignableFrom(fileObject.getClass())) {
            VersionFileObject version = Casts.cast(fileObject);
            getObjectArgs.versionId(version.getVersionId());
        }

        return SystemException.convertSupplier(() -> getObject(getObjectArgs.build()), "[minio async template] getObject error");
    }

    public CompletableFuture<ObjectWriteResponse> upload(FileObject object,
                                                         InputStream file,
                                                         long size,
                                                         String contentType) {
        return makeBucketIfNotExists(object)
                .thenCompose(makeBucketResult -> doUpload(object, file, size, contentType));
    }

    private CompletableFuture<ObjectWriteResponse> doUpload(FileObject object,
                                                            InputStream inputStream,
                                                            long size,
                                                            String contentType) {
        if (size > PART_SIZE) {
            return putObjectMultipart(object, inputStream);
        } else {
            return putObject(object, inputStream, size, contentType);
        }
    }

    public CompletableFuture<CreateMultipartUploadResponse> createMultipartUploadAsync(FileObject object) {
        return SystemException.convertSupplier(()->
                createMultipartUploadAsync(
                        object.getBucketName(),
                        object.getRegion(),
                        object.getObjectName(),
                        Objects.nonNull(object.getExtraHeaders()) ? Multimaps.forMap(object.getExtraHeaders()) : null,
                        Objects.nonNull(object.getExtraQueryParams()) ? Multimaps.forMap(object.getExtraQueryParams()) : null
                ),
                "[minio async template] createMultipartUpload error");
    }

    public CompletableFuture<ObjectWriteResponse> putObjectMultipart(FileObject object, InputStream file) {
        if (object instanceof UserMetadataFileObject userMetadataFileObject
                && MapUtils.isNotEmpty(userMetadataFileObject.getUserMetadata())) {
            userMetadataFileObject.getUserMetadata().forEach((k, v) -> object.getExtraHeaders().put(k, v));
        }

        return createMultipartUploadAsync(object).thenCompose(uploadResult ->
                uploadPart(
                        object,
                        file,
                        uploadResult.result().uploadId()
                )
                .thenCompose(parts ->
                        completeMultipartUploadAsync(object, parts.toArray(Part[]::new), uploadResult.result().uploadId())
                )
        );
    }

    public CompletableFuture<ObjectWriteResponse> completeMultipartUploadAsync(FileObject object, Part[] parts, String uploadIds) {
        return SystemException.convertSupplier(() ->
                completeMultipartUploadAsync(
                        object.getBucketName(),
                        object.getRegion(),
                        object.getObjectName(),
                        uploadIds,
                        parts,
                        Objects.nonNull(object.getExtraHeaders()) ? Multimaps.forMap(object.getExtraHeaders()) : null,
                        Objects.nonNull(object.getExtraQueryParams()) ? Multimaps.forMap(object.getExtraQueryParams()) : null
                )
                ,
                "[minio async template] completeMultipartUpload error"
        );
    }

    public CompletableFuture<List<Part>> uploadPart(
            FileObject object,
            InputStream inputStream,
            String uploadId) {

        // 1. 创建临时目录（同步操作，但快速完成）
        File tempDir = createUploadPartTempDir();

        // 2. 异步读取流并生成分片任务
        return CompletableFuture.supplyAsync(() -> readUploadPartStreamToFutures(inputStream, tempDir, object, uploadId), ioExecutor)
                .thenCompose(this::combineUploadPartFutures)
                .whenComplete((parts, ex) -> cleanupUploadPartTempDir(tempDir, ex));
    }

    private void cleanupUploadPartTempDir(File tempDir, Throwable ex) {
        try {
            if (tempDir.exists()) {
                FileUtils.deleteDirectory(tempDir);
            }
        } catch (IOException e) {
            LOGGER.error("[minio async template] cleanup error", e);
        }
        if (ex != null) {
            LOGGER.error("[minio async template] upload part failed", ex);
        }
    }

    private CompletableFuture<List<Part>> combineUploadPartFutures(List<CompletableFuture<Part>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    private List<CompletableFuture<Part>> readUploadPartStreamToFutures(InputStream is,
                                                                        File tempDir,
                                                                        FileObject object,
                                                                        String uploadId) {
        List<CompletableFuture<Part>> partFutures = new ArrayList<>();
        byte[] buffer = new byte[PART_SIZE];
        int partNumber = 1;
        int bytesRead;

        String filename = StringUtils.substringBefore(object.getObjectName(), Casts.DOT);

        try {
            while ((bytesRead = is.read(buffer)) != -1) {
                final int currentPartNumber = partNumber;
                final byte[] chunk = Arrays.copyOf(buffer, bytesRead);

                // 为每个分片创建异步任务链
                CompletableFuture<Part> future = CompletableFuture
                        .supplyAsync(() -> writeToUploadPartTempFile(tempDir, filename, currentPartNumber, chunk), ioExecutor)
                        .thenCompose(partFile ->
                                uploadPartAsync(object, uploadId, partFile, currentPartNumber)
                                        .whenComplete((res, ex) ->
                                                SystemException.convertRunnable(
                                                        () -> Files.deleteIfExists(partFile.toPath()),
                                                        "[minio async template] Files.deleteIfExists"
                                                )
                                        )
                        );
                partFutures.add(future);
                partNumber++;
            }
            return partFutures;
        } catch (IOException e) {
            throw new SystemException("[minio async template] Failed to read input stream", e);
        }
    }

    public CompletableFuture<Part> uploadPartAsync(FileObject object,
                                                 String uploadId,
                                                 File partFile,
                                                 int partNumber) {

        try (FileInputStream fis = new FileInputStream(partFile)) {
            // 假设 minioClient.uploadPartAsync 返回 CompletableFuture<UploadPartResponse>
            return uploadPartAsync(object, uploadId, fis, partFile.length(), partNumber);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Part> uploadPartAsync(FileObject object,
                                                   String uploadId,
                                                   InputStream inputStream,
                                                   long size,
                                                   int partNumber) throws Exception {
        return uploadPartAsync(
                    object.getBucketName(),
                    object.getRegion(),
                    object.getObjectName(),
                    inputStream,
                    size,
                    uploadId,
                    partNumber,
                    Objects.nonNull(object.getExtraHeaders()) ? Multimaps.forMap(object.getExtraHeaders()) : null,
                    Objects.nonNull(object.getExtraQueryParams()) ? Multimaps.forMap(object.getExtraQueryParams()) : null
                )
                .thenApply(response -> new Part(partNumber, response.etag()));
    }

    private File writeToUploadPartTempFile(File dir, String filename, int partNumber, byte[] chunk) {
        File file = new File(dir, filename + Casts.UNDERSCORE + System.nanoTime() + Casts.UNDERSCORE + partNumber);
        SystemException.convertRunnable(() -> Files.write(file.toPath(), chunk), "[minio async template] Files.write error");
        return file;
    }

    private File createUploadPartTempDir() {
        String pathName = getMinioProperties().getUploadPartTempFilePath()
                + Casts.UNDERSCORE
                + System.nanoTime();
        File tempDir = new File(pathName);
        SystemException.isTrue(tempDir.mkdirs(), "[minio async template] File.mkdir error");
        return tempDir;
    }

    public CompletableFuture<ObjectWriteResponse> putObject(FileObject object,
                                                            InputStream file,
                                                            long size,
                                                            String contentType) {
        PutObjectArgs.Builder args = PutObjectArgs
                .builder()
                .bucket(object.getBucketName().toLowerCase())
                .region(object.getRegion())
                .object(object.getObjectName())
                .stream(file, size, -1)
                .contentType(contentType)
                .extraHeaders(object.getExtraHeaders())
                .extraQueryParams(object.getExtraQueryParams());

        if (object instanceof UserMetadataFileObject userMetadataFileObject) {
            args.userMetadata(userMetadataFileObject.getUserMetadata());
        }

        return SystemException
                .convertSupplier(() -> putObject(args.build()), "[minio async template] putObject error");
    }

    /**
     * 将查询结果转换为 ObjectItem 对象
     *
     * @param results 查询结果
     * @return ObjectItem 对象集合
     */
    public List<ObjectItem> covertObjectItem(Iterable<Result<Item>> results) throws Exception {

        List<ObjectItem> result = new LinkedList<>();

        for (Result<Item> itemResult : results) {
            Item item = itemResult.get();
            result.add(new ObjectItem(item));
        }

        return result;
    }

    /**
     * 读取桶的文件内容值并将 json 转换为目标类型
     *
     * @param fileObject  文件对象描述
     * @param targetClass 目标类型 class
     * @param <T>         目标类型
     * @return 目标类型对象
     */
    public <T> CompletableFuture<T> readJsonValue(FileObject fileObject, Class<T> targetClass) {
        return getObject(fileObject)
                .thenApply(v -> SystemException
                        .convertSupplier(
                                () -> objectMapper.readValue(v, targetClass),
                                "[minio async template] objectMapper.readValue(FileObject fileObject, Class<T> targetClass) error"
                        )
                );
    }

    /**
     * 读取桶的文件内容值并将 json 转换为目标类型
     *
     * @param fileObject 文件对象描述
     * @param javaType   目标 java 类型
     * @param <T>        目标类型
     * @return 目标类型对象
     */
    public <T> CompletableFuture<T> readJsonValue(FileObject fileObject, JavaType javaType) {
        return getObject(fileObject)
                .thenApply(v -> SystemException
                        .convertSupplier(
                                () -> objectMapper.readValue(v, javaType),
                                "[minio async template] objectMapper.readValue(FileObject fileObject, JavaType javaType) error"
                        )
                );
    }

    public CompletableFuture<ObjectWriteResponse> copyObject(FileObject source, FileObject target)  {
        CopySource copySource = CopySource
                .builder()
                .bucket(source.getBucketName().toLowerCase())
                .region(source.getRegion())
                .object(source.getObjectName())
                .extraHeaders(Objects.isNull(source.getExtraHeaders()) ? null: source.getExtraHeaders())
                .extraQueryParams(Objects.isNull(source.getExtraQueryParams()) ? null: source.getExtraQueryParams())
                .build();

        return deleteObject(target, false)
                .thenApplyAsync(result -> CopyObjectArgs
                        .builder()
                        .bucket(target.getBucketName().toLowerCase())
                        .region(target.getRegion())
                        .object(Objects.toString(target.getObjectName(), target.getObjectName()))
                        .source(copySource)
                        .extraHeaders(Objects.isNull(target.getExtraHeaders()) ? null: target.getExtraHeaders())
                        .extraQueryParams(Objects.isNull(target.getExtraQueryParams()) ? null: target.getExtraQueryParams())
                        .build())
                .thenComposeAsync(args ->  SystemException.convertSupplier(() -> copyObject(args), "[minio async template] copyObject error"));

    }

    /**
     * 读取桶的文件内容值并将 json 转换为目标类型
     *
     * @param fileObject    文件对象描述
     * @param typeReference 目标泛型类型
     * @param <T>           目标类型
     * @return 目标类型对象
     */
    public <T> CompletableFuture<T> readJsonValue(FileObject fileObject, TypeReference<T> typeReference) {
        return getObject(fileObject)
                .thenApply(v -> SystemException
                        .convertSupplier(
                                () -> objectMapper.readValue(v, typeReference),
                                "[minio async template] objectMapper.readValue(FileObject fileObject, TypeReference<T> typeReference) error"
                        )
                );
    }

    /**
     * 将对象以 json 的格式写入到指定的桶和文件中
     *
     * @param fileObject 文件对象描述
     * @param value      对象值
     */
    public CompletableFuture<ObjectWriteResponse> writeJsonValue(FileObject fileObject, Object value) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, value);
        outputStream.flush();

        byte[] bytes = outputStream.toByteArray();
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        return upload(fileObject, arrayInputStream, bytes.length, MediaType.APPLICATION_JSON_VALUE);
    }
}
