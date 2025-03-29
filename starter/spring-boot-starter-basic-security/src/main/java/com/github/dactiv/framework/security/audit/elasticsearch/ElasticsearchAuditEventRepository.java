package com.github.dactiv.framework.security.audit.elasticsearch;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.JsonData;
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
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.BaseQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * es 审计事件仓库实现
 *
 * @author maurice.chen
 */
public class ElasticsearchAuditEventRepository extends AbstractExtendAuditEventRepository<BoolQuery.Builder> {

    public static final String MAPPING_FILE_PATH = "elasticsearch/plugin-audit-mapping.json";

    private final static Logger LOGGER = LoggerFactory.getLogger(ElasticsearchAuditEventRepository.class);

    private final ElasticsearchOperations elasticsearchOperations;

    private final StoragePositioningGenerator storagePositioningGenerator;

    public ElasticsearchAuditEventRepository(List<AuditEventRepositoryInterceptor<BoolQuery.Builder>> interceptors,
                                             ElasticsearchOperations elasticsearchOperations,
                                             StoragePositionProperties storagePositionProperties) {
        super(interceptors);
        this.elasticsearchOperations = elasticsearchOperations;

        this.storagePositioningGenerator = new SpringElStoragePositioningGenerator(storagePositionProperties);
    }

    @Override
    public void doAdd(AuditEvent event) {

        IdAuditEvent idAuditEvent = new IdAuditEvent(event);

        try {

            String index = storagePositioningGenerator.generatePositioning(idAuditEvent).toLowerCase();
            if (event instanceof StoragePositioningAuditEvent storagePositioningAuditEvent) {
                index = storagePositioningAuditEvent.getStoragePositioning();
            }

            IndexCoordinates indexCoordinates = IndexCoordinates.of(index);
            IndexOperations indexOperations = elasticsearchOperations.indexOps(indexCoordinates);
            createIndexIfNotExists(indexOperations, MAPPING_FILE_PATH);

            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(idAuditEvent.getId())
                    .withObject(event)
                    .build();

            elasticsearchOperations.index(indexQuery, indexCoordinates);

        } catch (Exception e) {
            LOGGER.warn("新增 elasticsearch {} 审计事件出现异常", event.getPrincipal(), e);
        }

    }

    @Override
    protected long doCount(FindMetadata<BoolQuery.Builder> metadata) {
        NativeQueryBuilder builder = new NativeQueryBuilder().withQuery(new Query(metadata.getTargetQuery().build()));
        try {
            return countData(builder.build(), metadata.getStoragePositioning());
        } catch (Exception e) {
            LOGGER.warn("统计 elasticsearch 审计事件出现异常", e);
            return 0;
        }
    }

    private long countData(NativeQuery query, String index) {
        return elasticsearchOperations.count(query, Map.class, IndexCoordinates.of(index));
    }

    public static void createIndexIfNotExists(IndexOperations indexOperations, String mappingFilePath) throws IOException {
        if (indexOperations.exists()) {
            return ;
        }

        indexOperations.create();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(mappingFilePath)) {
            Map<String, Object> mapping = Casts.getObjectMapper().readValue(input, Casts.MAP_TYPE_REFERENCE);
            indexOperations.putMapping(Document.from(mapping));
        }
    }


    @Override
    protected List<AuditEvent> doFind(FindMetadata<BoolQuery.Builder> metadata) {
        NativeQueryBuilder builder = new NativeQueryBuilder()
                .withQuery(new Query(metadata.getTargetQuery().build()))
                .withSort(SortOptions.of(s -> s.field(f -> f.field(RestResult.DEFAULT_TIMESTAMP_NAME).order(SortOrder.Desc))));

        Object number = metadata.getQuery().get(PageRequest.NUMBER_FIELD_NAME);
        Object size = metadata.getQuery().get(PageRequest.SIZE_FIELD_NAME);
        if (Objects.nonNull(number) && Objects.nonNull(size)) {
            builder.withPageable(
                    org.springframework.data.domain.PageRequest.of(
                            NumberUtils.toInt(number.toString()),
                            NumberUtils.toInt(size.toString())
                    )
            );
        }

        try {
            return findData(builder.build(), metadata.getStoragePositioning());
        } catch (Exception e) {
            LOGGER.warn("查询 elasticsearch 审计事件出现异常", e);
            return new LinkedList<>();
        }
    }

    public List<AuditEvent> findData(BaseQuery query, String index) {
        return elasticsearchOperations
                .search(query, Map.class, IndexCoordinates.of(index))
                .stream()
                .map(SearchHit::getContent)
                .map(c -> createAuditEvent(Casts.cast(c)))
                .collect(Collectors.toList());
    }

    @Override
    public AuditEvent get(StringIdEntity idEntity) {

        String index = storagePositioningGenerator.generatePositioning(idEntity).toLowerCase();
        try {
            //noinspection unchecked
            Map<String, Object> map = elasticsearchOperations.get(idEntity.getId(), Map.class, IndexCoordinates.of(index));
            if (MapUtils.isEmpty(map)) {
                return null;
            }
            return createAuditEvent(map);
        } catch (Exception e) {
            LOGGER.warn("通过 ID 查询索引 [{}] 出现错误", index, e);
        }

        return null;
    }

    public String getIndexName(Instant instant) {
        StringIdEntity id = new StringIdEntity();
        id.setCreationTime(java.sql.Date.from(instant));
        return storagePositioningGenerator.generatePositioning(id).toLowerCase();
    }

    /**
     * 创建查询条件
     *
     * @param after     在什么时间之后的
     * @param query     查询条件
     *
     * @return 查询条件
     */
    protected BoolQuery.Builder createQuery(Instant after, Map<String, Object> query) {

        BoolQuery.Builder queryBuilder = QueryBuilders.bool();

        String type = query.getOrDefault(IdAuditEvent.TYPE_FIELD_NAME, StringUtils.EMPTY).toString();
        if (StringUtils.isNotBlank(type)) {
            queryBuilder = queryBuilder.must(m -> m.term(t -> t.field(IdAuditEvent.TYPE_FIELD_NAME).value(type)));
        }

        if (Objects.nonNull(after)) {
            queryBuilder = queryBuilder.must(m -> m.range(r -> r.field(RestResult.DEFAULT_TIMESTAMP_NAME).gte(JsonData.of(after.getEpochSecond()))));
        }

        String principal = query.getOrDefault(IdAuditEvent.PRINCIPAL_FIELD_NAME, StringUtils.EMPTY).toString();
        if (StringUtils.isNotBlank(principal)) {
            queryBuilder = queryBuilder.must(m -> m.term(t -> t.field(IdAuditEvent.PRINCIPAL_FIELD_NAME).value(principal)));
        }

        return queryBuilder;
    }

    @Override
    protected FindMetadata<BoolQuery.Builder> createFindEntity(BoolQuery.Builder targetQuery,
                                                               Instant after,
                                                               Map<String, Object> query) {
        return new FindMetadata<>(targetQuery,getIndexName(after), query, after);
    }

    public ElasticsearchOperations getElasticsearchOperations() {
        return elasticsearchOperations;
    }
}
