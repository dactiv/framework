package com.github.dactiv.framework.commons.minio;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author maurice.chen
 */
public class CopyFileObject implements Serializable {

    @Serial
    private static final long serialVersionUID = -8412856701078392621L;

    /**
     * 原文件对象
     */
    private FileObject source;

    /**
     * 目标文件对象
     */
    private FileObject target;

    public CopyFileObject() {
    }

    public CopyFileObject(FileObject source, FileObject target) {
        this.source = source;
        this.target = target;
    }

    public FileObject getSource() {
        return source;
    }

    public void setSource(FileObject source) {
        this.source = source;
    }

    public FileObject getTarget() {
        return target;
    }

    public void setTarget(FileObject target) {
        this.target = target;
    }
}
