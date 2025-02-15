package com.github.dactiv.framework.security.audit;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.commons.id.IdEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.audit.AuditEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractExtendAuditEventRepository implements ExtendAuditEventRepository {

    private final List<AuditEventRepositoryInterceptor> interceptors;

    public AbstractExtendAuditEventRepository(List<AuditEventRepositoryInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public void add(AuditEvent event) {

        for (AuditEventRepositoryInterceptor interceptor : interceptors) {
            if (!interceptor.preAddHandle(event)) {
                return ;
            }
        }
        // 统一将 data 转换为 map，让一下没有构造函数的对象可以反序列化
        AuditEvent convert = new AuditEvent(
                event.getTimestamp(),
                event.getPrincipal(),
                event.getType(),
                Casts.convertValue(event.getData(), Casts.MAP_TYPE_REFERENCE)
        );
        doAdd(convert);

        interceptors.forEach(i -> i.postAddHandle(event));
    }

    protected abstract void doAdd(AuditEvent event);

    /**
     * 创建审计事件
     *
     * @param map map 数据源
     *
     * @return 审计事件
     */
    protected AuditEvent createAuditEvent(Map<String, Object> map) {
        Object timestamp = map.get(RestResult.DEFAULT_TIMESTAMP_NAME);

        Instant instant;
        if (timestamp instanceof Date) {
            Date date = Casts.cast(timestamp);
            instant = date.toInstant();
        } else if (timestamp instanceof Instant) {
            instant = Casts.cast(timestamp);
        } else if (timestamp instanceof Long){
            Long epochMilli = Casts.cast(timestamp);
            instant = Instant.ofEpochMilli(epochMilli);
        } else if (timestamp instanceof String) {
            String string = timestamp.toString();
            LocalDateTime localDateTime = LocalDateTime.parse(string);
            instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        } else {
            throw new SystemException("找不到 " + RestResult.DEFAULT_TIMESTAMP_NAME + " 的数据转换支持");
        }

        String principal = map.get(IdAuditEvent.PRINCIPAL_FIELD_NAME).toString();
        String type = map.get(IdAuditEvent.TYPE_FIELD_NAME).toString();

        Map<String, Object> data = Casts.cast(map.getOrDefault(RestResult.DEFAULT_DATA_NAME, new LinkedHashMap<>()));
        String id = map.getOrDefault(IdEntity.ID_FIELD_NAME, StringUtils.EMPTY).toString();
        return new IdAuditEvent(id, instant, principal, type, data);
    }
}
