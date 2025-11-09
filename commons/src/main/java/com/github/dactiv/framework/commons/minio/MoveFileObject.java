package com.github.dactiv.framework.commons.minio;

import java.io.Serial;

/**
 * 移动文件对象，用于拷贝或剪切文件对象使用
 *
 * @author maurice.chen
 */
public class MoveFileObject extends CopyFileObject {

    @Serial
    private static final long serialVersionUID = -8637215027233473463L;

    /**
     * 删除源文件后是否删除空桶
     */
    private boolean deleteBucketIfEmpty;

    public MoveFileObject() {
    }

    public MoveFileObject(FileObject source, FileObject target) {
        this(source, target, false);
    }

    public MoveFileObject(FileObject source, FileObject target, boolean deleteBucketIfEmpty) {
        super(source, target);
        this.deleteBucketIfEmpty = deleteBucketIfEmpty;
    }

    public boolean isDeleteBucketIfEmpty() {
        return deleteBucketIfEmpty;
    }

    public void setDeleteBucketIfEmpty(boolean deleteBucketIfEmpty) {
        this.deleteBucketIfEmpty = deleteBucketIfEmpty;
    }
}
