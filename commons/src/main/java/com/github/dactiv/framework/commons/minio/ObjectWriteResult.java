package com.github.dactiv.framework.commons.minio;

import org.springframework.util.DigestUtils;

import java.io.Serial;

/**
 * 对象写入成功后的结果集
 *
 * @author maurice.chen
 */
public class ObjectWriteResult extends FileObject {

    @Serial
    private static final long serialVersionUID = -2136072754715740876L;

    /**
     * minio e 标签名称
     */
    public static final String MINIO_ETAG = "etag";

    public static final String MINIO_ETAG_QUOTATION_MARKS = "\"";

    /**
     * minio e 标签
     */
    private String etag;

    public ObjectWriteResult() {
    }

    public ObjectWriteResult(String etag) {
        this.etag = etag;
    }

    public ObjectWriteResult(String bucketName, String objectName, String etag) {
        super(bucketName, objectName);
        this.etag = etag;
    }

    public ObjectWriteResult(Bucket bucket, String objectName, String etag) {
        super(bucket, objectName);
        this.etag = etag;
    }

    public ObjectWriteResult(String bucketName, String region, String objectName, String etag) {
        super(bucketName, region, objectName);
        this.etag = etag;
    }

    public ObjectWriteResult(FileObject fileObject, String etag) {
        this(fileObject.getBucketName(), fileObject.getRegion(), fileObject.getObjectName(), etag);
    }

    public static ObjectWriteResult of(FileObject fileObject, String etag) {
        return new ObjectWriteResult(fileObject, etag);
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return DigestUtils.md5DigestAsHex((getBucketName() + getObjectName()).getBytes());
    }
}
