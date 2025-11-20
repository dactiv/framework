package com.github.dactiv.framework.minio;

import io.minio.messages.Item;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 对象项类，用于在获取的内容 {@link Item} 时，能够序列化成 json 使用
 *
 * @author maurice.chen
 */
public class ObjectItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 5808561181950704489L;

    private final Item item;

    public ObjectItem(Item item) {
        this.item = item;
    }

    public String getObjectName(){
        return item.objectName();
    }

    public String getEtag() {
        String etag = item.etag();
        if (StringUtils.startsWith(etag, "\"") && (StringUtils.endsWith(etag, "\""))) {
            etag = StringUtils.unwrap(etag, "\"");
        }
        return etag;
    }

    public LocalDateTime getLastModified() {
        try {
            return item.lastModified().toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    public long getSize() {
        return item.size();
    }

    public Map<String, String> getUserMetadata() {
        return item.userMetadata();
    }

    public String getOwnerId() {
        if(Objects.isNull(item.owner())) {
            return null;
        }
        return item.owner().id();
    }

    public String getOwnerName() {
        if(Objects.isNull(item.owner())) {
            return null;
        }
        return item.owner().displayName();
    }

    public String getStorageClass() {
        return item.storageClass();
    }

    public boolean isLatest() {
        return item.isLatest();
    }

    public String getVersionId() {
        return item.versionId();
    }

    public String getUserTags() {
        return item.userTags();
    }

    public boolean isDir() {
        return item.isDir();
    }
}
