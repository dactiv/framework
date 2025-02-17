package com.github.dactiv.framework.security.audit.mongo;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.commons.id.StringIdEntity;
import com.github.dactiv.framework.commons.page.PageRequest;
import com.github.dactiv.framework.security.StoragePositionProperties;
import com.github.dactiv.framework.security.audit.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

/**
 * mongo 审计事件仓库实现
 *
 * @author maurice.chen
 */
public class MongoAuditEventRepository extends AbstractExtendAuditEventRepository<Criteria> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoAuditEventRepository.class);

    private final MongoTemplate mongoTemplate;

    private final StoragePositioningGenerator storagePositioningGenerator;

    public MongoAuditEventRepository(List<AuditEventRepositoryInterceptor<Criteria>> interceptors,
                                     MongoTemplate mongoTemplate,
                                     StoragePositionProperties storagePositionProperties) {
        super(interceptors);
        this.mongoTemplate = mongoTemplate;

        this.storagePositioningGenerator = new SpringElStoragePositioningGenerator(storagePositionProperties);
    }

    @Override
    public void doAdd(AuditEvent event) {

        IdAuditEvent idAuditEvent = new IdAuditEvent(event);
        if (IdAuditEvent.class.isAssignableFrom(event.getClass())) {
            idAuditEvent = Casts.cast(event);
        }

        try {
            String index = storagePositioningGenerator.generatePositioning(idAuditEvent).toLowerCase();
            if (event instanceof StoragePositioningAuditEvent storagePositioningAuditEvent) {
                index = storagePositioningAuditEvent.getStoragePositioning();
            }
            mongoTemplate.save(idAuditEvent, index);
        } catch (Exception e) {
            LOGGER.error("新增 mongo {} 审计事件出现异常", event.getPrincipal(), e);
        }

    }

    @Override
    public List<AuditEvent> find(String principal, Instant after, String type) {
        Map<String, Object> query = new LinkedHashMap<>();

        query.put(IdAuditEvent.PRINCIPAL_FIELD_NAME, principal);
        query.put(IdAuditEvent.TYPE_FIELD_NAME, type);
        return find(after, query);
    }

    @Override
    protected Criteria createQuery(Instant after, Map<String, Object> query) {
        Criteria criteria = new Criteria();

        String principal = query.getOrDefault(IdAuditEvent.PRINCIPAL_FIELD_NAME, StringUtils.EMPTY).toString();
        if (StringUtils.isNotBlank(principal)) {
            criteria = criteria.and(IdAuditEvent.PRINCIPAL_FIELD_NAME).is(principal);
        }

        String type = query.getOrDefault(IdAuditEvent.TYPE_FIELD_NAME, StringUtils.EMPTY).toString();
        if (StringUtils.isNotBlank(type)) {
            criteria = criteria.and(IdAuditEvent.TYPE_FIELD_NAME).is(type);
        }

        if (Objects.nonNull(after)) {
            criteria = criteria.and(RestResult.DEFAULT_TIMESTAMP_NAME).gte(Date.from(after));
        }

        return criteria;
    }

    @Override
    protected List<AuditEvent> doFind(Criteria targetQuery, Instant after, Map<String, Object> query) {

        Query mongodbQuery = new Query(targetQuery)
                .with(Sort.by(Sort.Order.desc(RestResult.DEFAULT_TIMESTAMP_NAME)));

        String index = getCollectionName(after).toLowerCase();

        Object number = query.get(PageRequest.NUMBER_FIELD_NAME);
        Object size = query.get(PageRequest.SIZE_FIELD_NAME);
        if (Objects.nonNull(number) && Objects.nonNull(size)) {
            mongodbQuery.with(
                    org.springframework.data.domain.PageRequest.of(
                            NumberUtils.toInt(number.toString()),
                            NumberUtils.toInt(size.toString())
                    )
            );
        }

        try {
            return findData(index, mongodbQuery);
        } catch (Exception e) {
            LOGGER.warn("查询 elasticsearch 审计事件出现异常", e);
            return new LinkedList<>();
        }
    }

    private List<AuditEvent> findData(String index, Query query) {
        List<AuditEvent> content = new LinkedList<>();
        try {
            List<AuditEvent> data = mongoTemplate
                    .find(query, Map.class, index).stream()
                    .map(d -> this.createAuditEvent(Casts.cast(d)))
                    .toList();
            content.addAll(data);
        } catch (Exception e) {
            LOGGER.error("查询集合 [{}] 出现错误", index, e);
        }

        return content;
    }

    @Override
    public AuditEvent get(StringIdEntity idEntity) {
        String index = storagePositioningGenerator.generatePositioning(idEntity).toLowerCase();

        try {
            Map<String, Object> map = Casts.cast(mongoTemplate.findById(idEntity.getId(), Map.class, index));
            if (MapUtils.isEmpty(map)) {
                return null;
            }
            return createAuditEvent(map);
        } catch (Exception e) {
            LOGGER.error("查询集合 [{}] 出现错误", index, e);
        }

        return null;
    }

    public String getCollectionName(Instant instant) {
        StringIdEntity id = new StringIdEntity();
        id.setCreationTime(Date.from(instant));
        return storagePositioningGenerator.generatePositioning(id).toLowerCase();
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
