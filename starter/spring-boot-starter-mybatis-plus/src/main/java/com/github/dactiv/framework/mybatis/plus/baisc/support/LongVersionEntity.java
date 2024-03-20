package com.github.dactiv.framework.mybatis.plus.baisc.support;

import com.baomidou.mybatisplus.annotation.Version;
import com.github.dactiv.framework.commons.id.number.LongIdEntity;
import com.github.dactiv.framework.mybatis.plus.baisc.VersionEntity;

import java.io.Serial;


/**
 * 整形，且带版本号的实体基类
 *
 * @param <V> 版本号类型
 *
 * @author maurice.chen
 */
public class LongVersionEntity<V> extends LongIdEntity implements VersionEntity<V, Long> {

    @Serial
    private static final long serialVersionUID = -5532855194695266443L;

    @Version
    private V version;

    @Override
    public void setVersion(V version) {
        this.version = version;
    }

    @Override
    public V getVersion() {
        return version;
    }
}
