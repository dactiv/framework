package com.github.dactiv.framework.mybatis.plus.audit;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.id.BasicIdentification;
import com.github.dactiv.framework.commons.id.IdEntity;
import com.github.dactiv.framework.mybatis.config.OperationDataTraceProperties;
import com.github.dactiv.framework.mybatis.enumerate.OperationDataType;
import com.github.dactiv.framework.mybatis.interceptor.audit.AbstractOperationDataTraceResolver;
import com.github.dactiv.framework.mybatis.interceptor.audit.OperationDataTraceRecord;
import com.github.dactiv.framework.mybatis.interceptor.audit.OperationDataTraceResolver;
import com.github.dactiv.framework.security.audit.IdAuditEvent;
import com.github.dactiv.framework.security.audit.IdStoragePositioningAuditEvent;
import com.github.dactiv.framework.security.audit.StoragePositioningAuditEvent;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.net.UnknownHostException;
import java.util.*;

/**
 * mybatis-plus 操作数据留痕仓库实现
 *
 * @author maurice.chen
 */
public class MybatisPlusOperationDataTraceResolver extends AbstractOperationDataTraceResolver implements OperationDataTraceResolver, ApplicationEventPublisherAware {

    public static final String WHERE_SEPARATE = "\\s+(?i:and|or)\\s+";

    private ApplicationEventPublisher applicationEventPublisher;

    public MybatisPlusOperationDataTraceResolver(OperationDataTraceProperties operationDataTraceProperties) {
        super(operationDataTraceProperties);
    }

    public MybatisPlusOperationDataTraceResolver(OperationDataTraceProperties operationDataTraceProperties,
                                                 ApplicationEventPublisher applicationEventPublisher) {
        super(operationDataTraceProperties);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public AuditEvent createAuditEvent(OperationDataTraceRecord record) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(OperationDataTraceRecord.SUBMIT_DATA_FIELD, record.getSubmitData());
        data.put(OperationDataTraceRecord.REMARK_FIELD, record.getRemark());
        return createAuditEvent(record, data);
    }

    @Override
    protected List<OperationDataTraceRecord> createInsertRecord(Insert insert, MappedStatement mappedStatement, Statement statement, Object parameter) throws Exception {

        if (parameter instanceof MapperMethod.ParamMap<?>) {
            MapperMethod.ParamMap<?> map = Casts.cast(parameter);
            Object entity = map.get(Constants.ENTITY);

            if (Objects.isNull(entity)) {
                return super.createInsertRecord(insert, mappedStatement, statement, parameter);
            }
            if (!BasicIdentification.class.isAssignableFrom(entity.getClass())) {
                return super.createInsertRecord(insert, mappedStatement, statement, parameter);
            }

            BasicIdentification<Object> basicIdentification = Casts.cast(entity);
            OperationDataTraceRecord record = createEntityIdOperationDataTraceRecord(
                    basicIdentification,
                    insert.getTable().getName(),
                    OperationDataType.INSERT
            );
            return Collections.singletonList(record);
        } else if (BasicIdentification.class.isAssignableFrom(parameter.getClass())) {
            BasicIdentification<Object> basicIdentification = Casts.cast(parameter);
            OperationDataTraceRecord record = createEntityIdOperationDataTraceRecord(
                    basicIdentification,
                    insert.getTable().getName(),
                    OperationDataType.INSERT
            );
            return Collections.singletonList(record);
        }

        return super.createInsertRecord(insert, mappedStatement, statement, parameter);
    }

    private EntityIdOperationDataTraceRecord createEntityIdOperationDataTraceRecord(BasicIdentification<Object> basicIdentification, String tableName, OperationDataType type) throws UnknownHostException {
        OperationDataTraceRecord result = super.createBasicOperationDataTraceRecord(
                type,
                tableName,
                new LinkedHashMap<>()
        );
        EntityIdOperationDataTraceRecord entityRecord = Casts.of(result, EntityIdOperationDataTraceRecord.class);
        entityRecord.setSubmitData(Casts.convertValue(basicIdentification, Casts.MAP_TYPE_REFERENCE));
        entityRecord.setEntityId(basicIdentification.getId());
        return entityRecord;
    }

    @Override
    protected List<OperationDataTraceRecord> createUpdateRecord(Update update, MappedStatement mappedStatement, Statement statement, Object parameter) throws Exception {
        return createUpdateOrDeleteRecord(update.getTable().getName(), OperationDataType.UPDATE, parameter);
    }

    private Map<String, Object> getUpdateModifiedMap(String slqSet, Object parameterObject) throws OgnlException {
        String[] fields = StringUtils.splitByWholeSeparator(slqSet, Casts.COMMA);
        Map<String, Object> result = new LinkedHashMap<>();
        for (String field : fields) {
            String name = StringUtils.substringBefore(field, Casts.EQ);
            String exp = StringUtils.substringAfter(field, Casts.EQ);
            Object value = getOgnlValue(exp, parameterObject);
            result.put(name, value);
        }

        return Collections.unmodifiableMap(result);
    }

    @Override
    public void saveOperationDataTraceRecord(List<OperationDataTraceRecord> records) throws Exception {
        String uuid = UUID.randomUUID().toString();
        for (OperationDataTraceRecord record : records) {
            AuditEvent auditEvent = createAuditEvent(record);
            if (Objects.isNull(auditEvent)) {
                continue;
            }

            AuditEvent publishEvent;
            if (auditEvent instanceof StoragePositioningAuditEvent storagePositioningAuditEvent) {
                publishEvent = new IdStoragePositioningAuditEvent(uuid, storagePositioningAuditEvent);
            } else {
                publishEvent = new IdAuditEvent(uuid, auditEvent);
            }

            applicationEventPublisher.publishEvent(new AuditApplicationEvent(publishEvent));
        }
    }

    public AuditEvent createAuditEvent(OperationDataTraceRecord record, Map<String, Object> data) {
        if (StringUtils.isNotEmpty(record.getStoragePositioning())) {
            return new StoragePositioningAuditEvent(
                    record.getStoragePositioning(),
                    record.getCreationTime().toInstant(),
                    record.getPrincipal().toString(),
                    getOperationDataTraceProperties().getAuditPrefixName() + Casts.UNDERSCORE + record.getTarget() + Casts.UNDERSCORE + record.getType(),
                    data
            );
        }
        return new AuditEvent(
                record.getCreationTime().toInstant(),
                record.getPrincipal().toString(),
                getOperationDataTraceProperties().getAuditPrefixName() + Casts.UNDERSCORE + record.getTarget() + Casts.UNDERSCORE + record.getType(),
                data
        );
    }

    private Object getIdValueExp(String sqlSegment, Object parameterObject) throws OgnlException {
        List<String> conditions = Arrays.asList(StringUtils.substringsBetween(sqlSegment, StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET));
        List<String> fields = conditions.stream().flatMap(s -> Arrays.stream(s.split(WHERE_SEPARATE))).toList();

        Object idValue = null;
        for (String field : fields) {
            String name = StringUtils.trim(StringUtils.substringBefore(field, Casts.EQ));
            if (!IdEntity.ID_FIELD_NAME.equals(name)) {
                continue;
            }
            String exp = StringUtils.trim(StringUtils.substringAfter(field, Casts.EQ));
            idValue = getOgnlValue(exp, parameterObject);
        }

        return idValue;
    }

    private Object getOgnlValue(String exp, Object parameterObject) throws OgnlException {
        Object value = Ognl.getValue(exp, parameterObject);
        if (Map.class.isAssignableFrom(value.getClass())) {
            Map<Object, Object> mapValue = Casts.cast(value);
            return mapValue.keySet().iterator().next();
        }

        return  value;
    }

    protected List<OperationDataTraceRecord> createUpdateOrDeleteRecord(String tableName,
                                                                        OperationDataType type,
                                                                        Object parameter) throws Exception {
        if (parameter instanceof MapperMethod.ParamMap<?>) {
            MapperMethod.ParamMap<?> map = Casts.cast(parameter);

            Object wrapper = null;
            if (map.containsKey(Constants.WRAPPER)) {
                wrapper = map.get(Constants.WRAPPER);
            }

            Object entity = null;
            if (map.containsKey(Constants.ENTITY)) {
                entity = map.get(Constants.ENTITY);
            }

            if (Objects.isNull(entity) && Objects.isNull(wrapper)) {
                Map<String, Object> submitData = Casts.convertValue(parameter, Casts.MAP_TYPE_REFERENCE);
                return Collections.singletonList(createBasicOperationDataTraceRecord(type, tableName, submitData));
            }

            if (Objects.nonNull(entity) && BasicIdentification.class.isAssignableFrom(entity.getClass())) {
                BasicIdentification<Object> basicIdentification = Casts.cast(entity);
                EntityIdOperationDataTraceRecord entityRecord = createEntityIdOperationDataTraceRecord(
                        basicIdentification,
                        tableName,
                        type
                );

                return Collections.singletonList(entityRecord);
            }

            if (Objects.nonNull(wrapper) && Wrapper.class.isAssignableFrom(wrapper.getClass())) {

                Wrapper<?> updateWrapper = Casts.cast(wrapper);
                String sqlSegment = updateWrapper.getSqlSegment();
                Object entityId = getIdValueExp(sqlSegment, map);

                if (Objects.nonNull(entityId)) {

                    OperationDataTraceRecord record = super.createBasicOperationDataTraceRecord(
                            type,
                            tableName,
                            new LinkedHashMap<>()
                    );

                    EntityIdOperationDataTraceRecord entityRecord = Casts.of(record, EntityIdOperationDataTraceRecord.class);

                    entityRecord.setEntityId(entityId);
                    if (OperationDataType.UPDATE.equals(type)) {
                        Map<String, Object> submitData = getUpdateModifiedMap(updateWrapper.getSqlSet(), parameter);
                        entityRecord.setSubmitData(submitData);
                    }
                    return Collections.singletonList(entityRecord);
                }
            }
        } else if (BasicIdentification.class.isAssignableFrom(parameter.getClass())) {
            BasicIdentification<Object> basicIdentification = Casts.cast(parameter);
            if (Objects.nonNull(basicIdentification.getId())) {
                OperationDataTraceRecord record = createEntityIdOperationDataTraceRecord(
                        basicIdentification,
                        tableName,
                        type
                );
                return Collections.singletonList(record);
            }
        }

        Map<String, Object> submitData = Casts.convertValue(parameter, Casts.MAP_TYPE_REFERENCE);
        return Collections.singletonList(createBasicOperationDataTraceRecord(type, tableName, submitData));

    }

    @Override
    protected List<OperationDataTraceRecord> createDeleteRecord(Delete delete, MappedStatement mappedStatement, Statement statement, Object parameter) throws Exception {
        if (Casts.isPrimitive(parameter.getClass())) {
            OperationDataTraceRecord record = createBasicOperationDataTraceRecord(
                    OperationDataType.DELETE,
                    delete.getTable().getName(),
                    new LinkedHashMap<>()
            );
            EntityIdOperationDataTraceRecord entityRecord = Casts.of(record, EntityIdOperationDataTraceRecord.class);
            entityRecord.setEntityId(parameter);
            return Collections.singletonList(entityRecord);
        }

        return createUpdateOrDeleteRecord(delete.getTable().getName(), OperationDataType.DELETE, parameter);

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }
}
