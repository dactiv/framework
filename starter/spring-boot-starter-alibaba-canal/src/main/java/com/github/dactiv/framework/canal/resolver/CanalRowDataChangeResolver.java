package com.github.dactiv.framework.canal.resolver;


import com.github.dactiv.framework.canal.CanalAdminService;
import com.github.dactiv.framework.canal.domain.CanalInstance;
import com.github.dactiv.framework.canal.domain.CanalMessage;
import com.github.dactiv.framework.canal.domain.CanalNodeServer;

/**
 * canal 行数据变更解析器, 用于当 canal 实例发现数据变更时，通过此接口定义一个规范，
 * 包装好特定的 {@link CanalMessage} 数据，让指定的实现类去完成自己的业务处理
 *
 * @see CanalAdminService#subscribe(CanalInstance, CanalNodeServer)
 *
 * @author maurice.chen
 */
public interface CanalRowDataChangeResolver {

    /**
     * 当 canal 发现数据变更时，处罚此方法
     *
     * @param message 变更的 消息 dto
     */
    void change(CanalMessage message);
}
