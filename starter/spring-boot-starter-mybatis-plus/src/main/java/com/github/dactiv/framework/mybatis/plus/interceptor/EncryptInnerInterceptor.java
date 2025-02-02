package com.github.dactiv.framework.mybatis.plus.interceptor;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.ReflectionUtils;
import com.github.dactiv.framework.crypto.algorithm.Base64;
import com.github.dactiv.framework.crypto.algorithm.CodecUtils;
import com.github.dactiv.framework.mybatis.plus.CryptoNullClass;
import com.github.dactiv.framework.mybatis.plus.CryptoService;
import com.github.dactiv.framework.mybatis.plus.DecryptService;
import com.github.dactiv.framework.mybatis.plus.EncryptService;
import com.github.dactiv.framework.mybatis.plus.annotation.EncryptProperties;
import com.github.dactiv.framework.mybatis.plus.annotation.Encryption;
import com.github.dactiv.framework.mybatis.plus.service.BasicService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 加密内部拦截器实现
 *
 * @author maurice.chen
 */
public class EncryptInnerInterceptor implements InnerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecryptInterceptor.class);

    public static final List<SqlCommandType> SUPPORT_COMMANDS = Arrays.asList(SqlCommandType.UPDATE, SqlCommandType.INSERT);

    public static final String PARAM_VALUE_PAIRS_NAME = "paramNameValuePairs";

    public static final String CONDITIONAL_SEGMENTATION_REX = "\\bAND\\b|\\bOR\\b";

    private boolean wrapperMode = false;

    private ApplicationContext applicationContext;

    public EncryptInnerInterceptor() {
    }

    public EncryptInnerInterceptor(boolean wrapperMode) {
        this.wrapperMode = wrapperMode;
    }

    public EncryptInnerInterceptor(boolean wrapperMode, ApplicationContext applicationContext) {
        this.wrapperMode = wrapperMode;
        this.applicationContext = applicationContext;
    }

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) {
        if (!SUPPORT_COMMANDS.contains(ms.getSqlCommandType())) {
            return;
        }

        if (parameter instanceof Map) {
            Map<String, Object> map = Casts.cast(parameter);
            encrypt(map, ms);
        } else {

            Map<CryptoService, List<Field>> fields = this.getCryptoFields(parameter.getClass(), ms.getSqlCommandType());
            if (MapUtils.isEmpty(fields)) {
                return;
            }
            doEncrypt(parameter, fields);
        }
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {

        if (!Map.class.isAssignableFrom(parameter.getClass())) {
            return ;
        }

        Map<String, Object> map = Casts.cast(parameter);
        Object ew = map.getOrDefault(Constants.WRAPPER, null);
        if (Objects.isNull(ew)) {
            return ;
        }
        AbstractWrapper<?,?,?> queryWrapper = Casts.cast(ew);
        String sqlSegment = queryWrapper.getExpression().getNormal().getSqlSegment();
        if (StringUtils.isEmpty(sqlSegment)) {
            return;
        }
        String sql = StringUtils.substringBetween(sqlSegment, Casts.LEFT_BRACKET, Casts.RIGHT_BRACKET);
        List<String> conditional = Arrays.asList(sql.split(CONDITIONAL_SEGMENTATION_REX));

        Class<?> entityClass = BasicService.getEntityClass(ms.getId());

        Map<CryptoService, List<Field>> cryptoServiceListMap = getCryptoFields(entityClass, ms.getSqlCommandType());
        encryptAndReplaceParamNameValuePairs(queryWrapper, conditional, cryptoServiceListMap);
    }

    public void encrypt(Map<String, Object> map, MappedStatement ms) {
        Object et = map.getOrDefault(Constants.ENTITY, null);
        if (Objects.nonNull(et)) {

            Map<CryptoService, List<Field>> fields = this.getCryptoFields(et.getClass(), ms.getSqlCommandType());
            if (MapUtils.isEmpty(fields)) {
                return;
            }
            this.doEncrypt(et, fields);
        } else if (wrapperMode && map.entrySet().stream().anyMatch(t -> Objects.equals(t.getKey(), Constants.WRAPPER))) {
            // update(LambdaUpdateWrapper) or update(UpdateWrapper)
            this.doEncrypt(map, ms);
        }
    }

    public void doEncrypt(Object entity, Map<CryptoService, List<Field>> fields) {
        for (Map.Entry<CryptoService, List<Field>> entry : fields.entrySet()) {
            EncryptService encryptService = Casts.cast(entry.getKey(), EncryptService.class);
            for (Field field : entry.getValue()) {
                Object value = ReflectionUtils.getFieldValue(entity, field);
                if (Objects.isNull(value) || Base64.isBase64(CodecUtils.toBytes(value.toString()))) {
                    continue;
                }
                try {
                    String text = encryptService.encrypt(value.toString());
                    ReflectionUtils.setFieldValue(entity, field.getName(), text);
                } catch (Exception e) {
                    LOGGER.warn("加密 [{}] 的 {} 字段出现异常:{},数据将不做任何处理", entity.getClass(), field.getName(), e.getMessage());
                }
            }

        }
    }

    public void doEncrypt(Map<String, Object> map, MappedStatement ms) {
        Object ew = map.get(Constants.WRAPPER);

        if (Objects.isNull(ew) || !AbstractWrapper.class.isAssignableFrom(ew.getClass())) {
            return ;
        }
        Class<?> entityClass = BasicService.getEntityClass(ms.getId());
        AbstractWrapper<?, ?, ?> updateWrapper = Casts.cast(ew);
        Map<CryptoService, List<Field>> fields = this.getCryptoFields(entityClass, ms.getSqlCommandType());
        if (MapUtils.isEmpty(fields)) {
            return ;
        }

        List<String> sqlSet = Arrays.asList(StringUtils.splitByWholeSeparator(updateWrapper.getSqlSet(), Casts.COMMA));

        encryptAndReplaceParamNameValuePairs(updateWrapper, sqlSet, fields);

    }

    public void encryptAndReplaceParamNameValuePairs(AbstractWrapper<?,?,?> wrapper, List<String> sqlExpressions, Map<CryptoService, List<Field>> fields) {
        Map<String, Object> paramNameValuePairs = Casts.cast(ReflectionUtils.getFieldValue(wrapper, PARAM_VALUE_PAIRS_NAME));

        for (Map.Entry<CryptoService, List<Field>> entry : fields.entrySet()) {
            List<String> sqlEncryptionFieldList = entry
                    .getValue()
                    .stream()
                    .map(f -> Casts.castCamelCaseToSnakeCase(f.getName()))
                    .flatMap(s -> findSqlSetField(s, sqlExpressions).stream())
                    .toList();

            for (String encryptionField : sqlEncryptionFieldList) {
                String el = StringUtils.substringAfter(encryptionField, Casts.EQ);
                String value = StringUtils.substringBetween(el, StandardBeanExpressionResolver.DEFAULT_EXPRESSION_PREFIX, StandardBeanExpressionResolver.DEFAULT_EXPRESSION_SUFFIX);
                String varName = StringUtils.substringAfterLast(value, Casts.DOT);

                EncryptService encryptService = Casts.cast(entry.getKey());
                Object paramValue = paramNameValuePairs.get(varName);
                if (Objects.isNull(paramValue)) {
                    continue;
                }
                String plainText = paramValue.toString();
                if (StringUtils.isEmpty(plainText) || Base64.isBase64(plainText)) {
                    continue;
                }
                paramNameValuePairs.put(varName, encryptService.encrypt(plainText));
            }
        }
    }

    public List<String> findSqlSetField(String field, List<String> sqlSet) {
        return sqlSet.stream().filter(set -> StringUtils.contains(set, field)).collect(Collectors.toList());
    }

    public EncryptService getEncryptService(String beanName, Class<? extends EncryptService> serviceClass) {
        EncryptService encryptService = null;
        if (Objects.nonNull(applicationContext)) {
            encryptService = getCryptoService(applicationContext, serviceClass, beanName);
        }

        if (Objects.isNull(encryptService) && serviceClass != CryptoNullClass.class) {
            encryptService = BeanUtils.instantiateClass(serviceClass);
        }

        return encryptService;
    }

    public static <T extends CryptoService> T getCryptoService(ApplicationContext applicationContext, Class<T> cryptoService, String beanName) {

        T result = null;

        if (StringUtils.isNotEmpty(beanName) && CryptoNullClass.class != cryptoService) {
            result = applicationContext.getBean(beanName, cryptoService);
        } else if (StringUtils.isNotEmpty(beanName)) {
            Object bean = applicationContext.getBean(beanName);
            if (!DecryptService.class.isAssignableFrom(bean.getClass())) {
                return null;
            }
            result = Casts.cast(bean);
        } else if (CryptoNullClass.class != cryptoService) {
            result = applicationContext.getBean(cryptoService);
        }

        return result;
    }

    private Map<CryptoService, List<Field>> getCryptoFields(Class<?> entityClass, SqlCommandType commandType) {
        Map<CryptoService, List<Field>> result = new LinkedHashMap<>();
        if (Objects.isNull(entityClass)) {
            return result;
        }
        List<Field> fields = ReflectionUtils.findFields(entityClass);

        Set<EncryptProperties> encrypts = AnnotatedElementUtils.findAllMergedAnnotations(entityClass, EncryptProperties.class);

        if (CollectionUtils.isNotEmpty(encrypts)) {
            for (EncryptProperties properties : encrypts) {
                EncryptService encryptService = getEncryptService(properties.beanName(), properties.serviceClass());
                List<Field> fieldList = result.computeIfAbsent(encryptService, k -> new LinkedList<>());
                fields
                        .stream()
                        .filter(f -> ArrayUtils.contains(properties.value(), f.getName()))
                        .filter(f -> fieldList.stream().noneMatch(l -> StringUtils.equals(l.getName(), f.getName())))
                        .forEach(fieldList::add);
            }
        }

        for (Field field : fields) {
            Encryption encryption = AnnotatedElementUtils.findMergedAnnotation(field, Encryption.class);
            if (Objects.isNull(encryption)) {
                continue;
            }

            if (isFieldNeverStrategy(field, commandType)) {
                continue;
            }

            EncryptService encryptService = getEncryptService(encryption.beanName(), encryption.serviceClass());
            List<Field> fieldList = result.computeIfAbsent(encryptService, k -> new LinkedList<>());
            fieldList.add(field);
        }
        return result;

    }

    private boolean isFieldNeverStrategy(Field field, SqlCommandType commandType) {
        TableField tableField = AnnotatedElementUtils.findMergedAnnotation(field, TableField.class);

        if (Objects.isNull(tableField)) {
            return false;
        }

        if (SqlCommandType.INSERT.equals(commandType) && FieldStrategy.NEVER.equals(tableField.insertStrategy())) {
            return true;
        }

        return SqlCommandType.INSERT.equals(commandType) && FieldStrategy.NEVER.equals(tableField.updateStrategy());
    }
}
