package com.github.dactiv.framework.mybatis.interceptor.audit;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.mybatis.config.OperationDataTraceProperties;
import com.github.dactiv.framework.mybatis.enumerate.OperationDataType;
import com.github.dactiv.framework.security.audit.SpringElStoragePositioningGenerator;
import com.github.dactiv.framework.security.audit.StoragePositioningGenerator;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 内存形式的操作数据留痕仓库实现
 *
 * @author maurice.chen
 */
public abstract class AbstractOperationDataTraceResolver implements OperationDataTraceResolver {

    private final DateFormat dateFormat;

    private final OperationDataTraceProperties operationDataTraceProperties;

    private StoragePositioningGenerator storagePositioningGenerator;

    public AbstractOperationDataTraceResolver(OperationDataTraceProperties operationDataTraceProperties) {
        this.operationDataTraceProperties = operationDataTraceProperties;
        if (Objects.nonNull(operationDataTraceProperties.getStoragePosition())) {
            storagePositioningGenerator = new SpringElStoragePositioningGenerator(operationDataTraceProperties.getStoragePosition());
        }
        this.dateFormat = new SimpleDateFormat(operationDataTraceProperties.getDateFormat());
    }

    @Override
    public List<OperationDataTraceRecord> createOperationDataTraceRecord(MappedStatement mappedStatement, Statement statement, Object parameter) throws Exception{
        List<OperationDataTraceRecord> createRecords = new LinkedList<>();
        if (statement instanceof Insert) {
            Insert insert = Casts.cast(statement);
            createRecords.addAll(createInsertRecord(insert, mappedStatement, statement, parameter));
        } else if (statement instanceof Update) {
            Update update = Casts.cast(statement);
            createRecords.addAll(createUpdateRecord(update, mappedStatement, statement, parameter));
        } else if (statement instanceof Delete) {
            Delete delete = Casts.cast(statement);
            createRecords.addAll(createDeleteRecord(delete, mappedStatement, statement, parameter));
        }

        List<OperationDataTraceRecord> result = new LinkedList<>(createRecords);
        if (Objects.nonNull(storagePositioningGenerator)) {
            List<OperationDataTraceRecord> storagePositioningRecords = new LinkedList<>();
            for (OperationDataTraceRecord record : createRecords) {
                OperationDataTraceRecord storagePositioning = Casts.of(record, OperationDataTraceRecord.class);
                storagePositioning.setStoragePositioning(storagePositioningGenerator.generatePositioning(record));
                storagePositioningRecords.add(storagePositioning);
            }
            result.addAll(storagePositioningRecords);
        }


        return result;
    }

    protected List<OperationDataTraceRecord> createDeleteRecord(Delete delete, MappedStatement mappedStatement, Statement statement, Object parameter) throws Exception {
        OperationDataTraceRecord result = createBasicOperationDataTraceRecord(
                OperationDataType.DELETE,
                delete.getTable().getName(),
                Casts.convertValue(parameter, Casts.MAP_TYPE_REFERENCE)
        );
        return Collections.singletonList(result);
    }

    protected List<OperationDataTraceRecord> createUpdateRecord(Update update, MappedStatement mappedStatement, Statement statement, Object parameter) throws Exception {
        OperationDataTraceRecord result = createBasicOperationDataTraceRecord(
                OperationDataType.UPDATE,
                update.getTable().getName(),
                Casts.convertValue(parameter, Casts.MAP_TYPE_REFERENCE)
        );
        return Collections.singletonList(result);
    }

    protected List<OperationDataTraceRecord> createInsertRecord(Insert insert,
                                                                MappedStatement mappedStatement,
                                                                Statement statement,
                                                                Object parameter) throws Exception {
        OperationDataTraceRecord result = createBasicOperationDataTraceRecord(
                OperationDataType.INSERT,
                insert.getTable().getName(),
                Casts.convertValue(parameter, Casts.MAP_TYPE_REFERENCE)
        );

        return Collections.singletonList(result);
    }

    protected OperationDataTraceRecord createBasicOperationDataTraceRecord(OperationDataType type,
                                                                           String target,
                                                                           Map<String, Object> submitData) throws UnknownHostException {
        OperationDataTraceRecord record = new OperationDataTraceRecord();

        record.setPrincipal(InetAddress.getLocalHost().getHostAddress());
        record.setType(type);
        record.setTarget(target);
        record.setSubmitData(submitData);
        record.setRemark(record.getPrincipal() + StringUtils.SPACE + dateFormat.format(record.getCreationTime()) +  StringUtils.SPACE + record.getType().getName());

        return record;
    }

    public OperationDataTraceProperties getOperationDataTraceProperties() {
        return operationDataTraceProperties;
    }

    public StoragePositioningGenerator getStoragePositioningGenerator() {
        return storagePositioningGenerator;
    }

    public void setStoragePositioningGenerator(StoragePositioningGenerator storagePositioningGenerator) {
        this.storagePositioningGenerator = storagePositioningGenerator;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }
}
