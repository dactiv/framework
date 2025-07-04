package com.github.dactiv.framework.commons;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dactiv.framework.commons.annotation.Description;
import com.github.dactiv.framework.commons.annotation.IgnoreField;
import com.github.dactiv.framework.commons.domain.metadata.TreeDescriptionMetadata;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.commons.jackson.serializer.DesensitizeSerializer;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import net.minidev.json.JSONArray;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.objenesis.instantiator.util.ClassUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 转型工具类
 *
 * @author maurice.chen
 **/
public abstract class Casts {

    private static final Logger LOGGER = LoggerFactory.getLogger(Casts.class);

    /**
     * 问号
     */
    public static final String QUESTION_MARK = "?";

    /**
     * 点符号
     */
    public static final String DOT = ".";

    public static final String VERSION_SPLIT_REGEX = "\\.";

    /**
     *
     */
    public static final String UNDERSCORE = "_";

    /**
     * 负极符号
     */
    public final static String NEGATIVE = "-";

    /**
     * 分号
     */
    public final static String SEMICOLON = ";";

    /**
     * 逗号
     */
    public final static String COMMA = ",";

    /**
     * 默认等于符号
     */
    public static final String EQ = "=";

    /**
     * 默认 and 符号
     */
    public static final String HTTP_AND = "&";

    /**
     * 路径变量开始符号
     */
    public static final String HTTP_PATH_VARIABLE_START = "{";

    /**
     * 路径变量结束符号
     */
    public static final String HTTP_PATH_VARIABLE_END = "}";

    /**
     * 左括号
     */
    public static final String LEFT_BRACKET = "(";

    /**
     * 有括号
     */
    public static final String RIGHT_BRACKET = ")";

    public static final String DEFAULT_DATE_FORMATTER_PATTERN = "yyyy-MM-dd";

    public static final String DEFAULT_TIME_FORMATTER_PATTERN = "HH:mm:ss";

    public static final String DEFAULT_DATE_TIME_FORMATTER_PATTERN = DEFAULT_DATE_FORMATTER_PATTERN + StringUtils.SPACE + DEFAULT_TIME_FORMATTER_PATTERN;

    /**
     *  map 类型范型引用
     */
    public static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    /**
     * map 参数类型引用
     */
    public static final ParameterizedTypeReference<Map<String, Object>> MAP_PARAMETERIZED_TYPE_REFERENCE = new ParameterizedTypeReference<>() {};

    /**
     * list map 类型范型引用
     */
    public static final TypeReference<List<Map<String, Object>>> LIST_MAP_TYPE_REFERENCE = new TypeReference<>() {};

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 设置 jackson objectMapper
     *
     * @param objectMapper objectMapper
     */
    public static void setObjectMapper(ObjectMapper objectMapper) {
        Casts.objectMapper = objectMapper;
    }

    /**
     * 将值转换成指定类型的对象
     *
     * @param value 值
     * @param type  指定类型
     * @param <T>   对象范型实体值
     * @return 指定类型的对象实例
     */
    public static <T> T convertValue(Object value, Class<T> type) {
        return objectMapper.convertValue(value, type);
    }

    /**
     * 将值转换成指定类型的对象
     *
     * @param value       值
     * @param toValueType 指定类型
     * @param <T>         对象范型实体值
     * @return 指定类型的对象实例
     */
    public static <T> T convertValue(Object value, JavaType toValueType) {
        return objectMapper.convertValue(value, toValueType);
    }

    /**
     * 将值转换成指定类型的对象
     *
     * @param value 值
     * @param type  引用类型
     * @param <T>   对象范型实体值
     * @return 指定类型的对象实例
     */
    public static <T> T convertValue(Object value, TypeReference<T> type) {
        return objectMapper.convertValue(value, type);
    }

    /**
     * 获取 object mapper
     *
     * @return object mapper
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * 将格式为 http query string 的字符串转型为成 MultiValueMap
     *
     * @param body 数据题
     * @return 转换后的 MultiValueMap 对象
     */
    public static MultiValueMap<String, String> castRequestBodyMap(String body) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();

        Arrays.stream(StringUtils.split(body, HTTP_AND)).forEach(b -> {
            String key = StringUtils.substringBefore(b, EQ);
            String value = StringUtils.substringAfter(b, EQ);
            result.add(key, value);
        });

        return result;
    }

    public static String toSnakeCase(String name) {
        if (StringUtils.isEmpty(name)) {
            return name;
        }
        StringBuilder result = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append(UNDERSCORE).append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * 将 MultiValueMap 对象转换为 name=value&amp;name2=value2&amp;name3=value3 格式字符串
     *
     * @param newRequestBody MultiValueMap 对象
     * @return 转换后的字符串
     */
    public static <K,V> String castRequestBodyMapToString(MultiValueMap<K, V> newRequestBody) {
        return castRequestBodyMapToString(newRequestBody, Object::toString);
    }

    /**
     * 将 MultiValueMap 对象转换为 name=value&amp;name2=value2&amp;name3=value3 格式字符串
     *
     * @param newRequestBody MultiValueMap 对象
     * @param function       处理字符串的功能
     * @return 转换后的字符串
     */
    public static <K,V> String castRequestBodyMapToString(MultiValueMap<K, V> newRequestBody, Function<V, String> function) {
        StringBuilder result = new StringBuilder();

        newRequestBody
                .forEach((key, value) -> value
                        .forEach(
                                v -> result
                                        .append(key)
                                        .append(EQ)
                                        .append(value.size() > 1 ? value.stream().map(function).collect(Collectors.toList()) : function.apply(value.get(0)))
                                        .append(HTTP_AND)
                        )
                );

        if (result.length() > 1) {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }

    /**
     * 将数 map 数据转换成 普通的 map 对象，如果值为 map 参数的值为1个以上的数组值，将该 key 对应的值转换成 list 对象
     *
     * @param map 要转换的 map 对象
     *
     * @return 新的 map 对象
     */
    public static <K,V> Map<K, Object> castArrayValueMapToObjectValueMap(Map<K, V[]> map) {
        return castArrayValueMapToObjectValueMap(map, s -> s);
    }

    /**
     * 将数组值的 map 数据转换成 MultiValueMap 对象
     *
     * @param map map 对象
     *
     * @return MultiValueMap
     *
     */
    public static <K,V> MultiValueMap<K, V> castMapToMultiValueMap(Map<K,V[]> map) {
        return castMapToMultiValueMap(map, false);
    }

    /**
     * 将数组值的 map 数据转换成 MultiValueMap 对象
     *
     * @param map map 对象
     * @param urlEncode 如果值为 String 类型是否 url 编码，true 是，否则 false
     *
     * @return MultiValueMap
     *
     */
    public static <K,V> MultiValueMap<K, V> castMapToMultiValueMap(Map<K,V[]> map, boolean urlEncode) {
        MultiValueMap<K,V> result = new LinkedMultiValueMap<>();

        for (Map.Entry<K, V[]> entry : map.entrySet()) {
            List<V> values = Arrays
                    .stream(entry.getValue())
                    .map(v -> urlEncode ? urlEncode(v.toString(), Charset.defaultCharset().name()) : v)
                    .map(v -> (V) v)
                    .collect(Collectors.toList());
            result.put(entry.getKey(), values);
        }

        return result;
    }

    public static String urlEncode(String value, String enc) {
        try {
            return URLEncoder.encode(value, enc);
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("url encode 时出现错误", e);
            return value;
        }
    }

    /**
     * 将 key 为 String， value 为 String 数组的 map 数据转换成 key 为 String，value 为 object 的 map 对象
     *
     * @param map      key 为 String， value 为 String 数组的 map
     * @param function 处理字符串的功能
     * @return key 为 String，value 为 object 的 map 对象
     */
    public static <K,V> Map<K, Object> castArrayValueMapToObjectValueMap(Map<K, V[]> map, Function<V, Object> function) {
        Map<K, Object> result = new LinkedHashMap<>();

        map.forEach((k, v) -> {
            if (v.length > 1) {
                result.put(k, Arrays.stream(v).map(function).collect(Collectors.toList()));
            } else {
                result.put(k, function.apply(v[0]));
            }
        });

        return result;
    }

    /**
     * 集合转换器的实现
     *
     * @author maurice.chen
     */
    @SuppressWarnings("rawtypes")
    private static class CollectionConverter implements Converter {

        @Override
        public <T> T convert(Class<T> type, Object value) {
            Class<?> typeInstance;

            if (type.isInterface() && Set.class.isAssignableFrom(type)) {
                typeInstance = LinkedHashSet.class;
            } else if (type.isInterface() && (List.class.isAssignableFrom(type) || Collection.class.isAssignableFrom(type))) {
                typeInstance = LinkedList.class;
            } else if (!type.isInterface()) {
                typeInstance = type;
            } else {
                typeInstance = value.getClass();
            }

            Object obj = ClassUtils.newInstance(typeInstance);
            Collection<?> collection = null;

            if (Collection.class.isAssignableFrom(obj.getClass())) {
                collection = (Collection<?>) obj;
            }

            if (collection == null) {
                return type.cast(value);
            }

            if (Collection.class.isAssignableFrom(value.getClass())) {
                Collection values = (Collection) value;
                collection.addAll(values);
            }

            return type.cast(obj);
        }

    }

    private static class MapConverter implements Converter {

        @Override
        public <T> T convert(Class<T> aClass, Object o) {
            if (String.class.isAssignableFrom(o.getClass())) {
                return SystemException.convertSupplier(() -> getObjectMapper().readValue(o.toString(), aClass), (String)null);
            }
            return convertValue(o, aClass);
        }
    }

    static {
        registerDateConverter(DEFAULT_DATE_FORMATTER_PATTERN, DEFAULT_DATE_TIME_FORMATTER_PATTERN);
        registerCollectionConverter();
        ConvertUtils.register(new MapConverter(), Map.class);
    }

    /**
     * 注册集合类型的转换器
     */
    private static void registerCollectionConverter() {
        ConvertUtils.register(new CollectionConverter(), Collection.class);
        ConvertUtils.register(new CollectionConverter(), List.class);
        ConvertUtils.register(new CollectionConverter(), ArrayList.class);
        ConvertUtils.register(new CollectionConverter(), LinkedList.class);
        ConvertUtils.register(new CollectionConverter(), Set.class);
        ConvertUtils.register(new CollectionConverter(), HashSet.class);
        ConvertUtils.register(new CollectionConverter(), LinkedHashSet.class);
    }

    /**
     * 注册一个时间类型的转换器,当前默认的格式为：yyyy-MM-dd
     *
     * @param patterns 日期格式
     */
    private static void registerDateConverter(String... patterns) {
        DateConverter dc = new DateConverter();
        dc.setUseLocaleFormat(true);
        dc.setPatterns(patterns);
        ConvertUtils.register(dc, Date.class);
    }

    /**
     * 通过路径获取 map 实体
     *
     * @param source map 数据源
     * @param path   路径，多个以点(".")分割
     * @return map 实体
     */
    public static Map<String, Object> getPathMap(Map<String, Object> source, String path) {

        Map<String, Object> result = new LinkedHashMap<>(source);

        String[] strings = StringUtils.split(path, DOT);

        for (String s : strings) {
            result = Casts.cast(result.get(s));
        }

        return result;

    }

    /**
     * 将 value 转型为返回值类型
     *
     * @param value 值
     * @param <T>   值类型
     * @return 转型后的值
     */
    public static <T> T cast(Object value) {
        if (value == null) {
            return null;
        }
        return (T) cast(value, value.getClass());
    }

    /**
     * 如果 value 不为 null 值，将 value 转型为返回值类型
     *
     * @param value 值
     * @param <T>   值类型
     * @return 转型后的值
     */
    public static <T> T castIfNotNull(Object value) {
        if (value == null) {
            return null;
        }

        return cast(value);
    }

    /**
     * 将 value 转型为返回值类型
     *
     * @param value 值
     * @param type  值类型 class
     * @param <T>   值类型
     * @return 转型后的值
     */
    public static <T> T cast(Object value, Class<T> type) {
        return (T) (value == null ? null : ConvertUtils.convert(value, type));
    }

    /**
     * 讲值转型为 Optional 类型
     *
     * @param value 值
     * @param <T>   值类型
     * @return Optional
     */
    public static <T> Optional<T> castOptional(Object value) {
        return Optional.ofNullable((T) value);
    }

    /**
     * 如果 value 不为 null 值，将 value 转型为返回值类型
     *
     * @param value 值
     * @param type  值类型 class
     * @param <T>   值类型
     * @return 转型后的值
     */
    public static <T> T castIfNotNull(Object value, Class<T> type) {

        if (value == null) {
            return null;
        }

        return cast(value, type);
    }

    /**
     * 设置 url 路径变量值
     *
     * @param url           url 路径
     * @param variableValue url 路径的变量对应值 map
     * @return 新的 url 路径
     */
    public static String setUrlPathVariableValue(String url, Map<String, String> variableValue) {

        String[] vars = StringUtils.substringsBetween(url, HTTP_PATH_VARIABLE_START, HTTP_PATH_VARIABLE_END);

        List<String> varList = Arrays.asList(vars);

        List<String> existList = varList
                .stream()
                .map(StringUtils::trimToEmpty)
                .filter(variableValue::containsKey)
                .toList();

        String temp = url;

        for (String s : existList) {
            String searchString = HTTP_PATH_VARIABLE_START + s + HTTP_PATH_VARIABLE_END;
            temp = StringUtils.replace(temp, searchString, variableValue.get(s));
        }

        return temp;
    }

    /**
     * 转换为描述元数据集合
     *
     * @param descriptionClasses 描述类集合
     *
     * @return 描述元数据集合
     */
    public static List<TreeDescriptionMetadata> convertDescriptionMetadata(List<Class<?>> descriptionClasses) {
        return descriptionClasses.stream().map(Casts::convertDescriptionMetadata).collect(Collectors.toList());
    }

    /**
     * 转换为描述元数据
     *
     * @param descriptionClass 描述类
     *
     * @return 描述元数据
     */
    public static TreeDescriptionMetadata convertDescriptionMetadata(Class<?> descriptionClass) {

        Description description = AnnotationUtils.findAnnotation(descriptionClass, Description.class);
        if (Objects.isNull(description)) {
            return null;
        }

        TreeDescriptionMetadata metadata = TreeDescriptionMetadata.of(
                descriptionClass.getSimpleName(),
                description.value()
        );

        List<Field> fields = ReflectionUtils.findFields(descriptionClass);
        fields
                .stream()
                .map(Casts::convertDescriptionMetadata)
                .filter(Objects::nonNull)
                .peek(f -> f.setParentId(metadata.getId()))
                .forEach(f -> metadata.getChildren().add(f));
        List<Method> methods = MethodUtils.getMethodsListWithAnnotation(descriptionClass, Description.class);

        methods
                .stream()
                .map(Casts::convertDescriptionMetadata)
                .filter(Objects::nonNull)
                .peek(f -> f.setParentId(metadata.getId()))
                .forEach(f -> metadata.getChildren().add(f));
        return metadata;

    }

    public static TreeDescriptionMetadata convertDescriptionMetadata(Method method) {
        Description description = AnnotationUtils.findAnnotation(method, Description.class);

        if (Objects.isNull(description)) {
            return null;
        }

        Class<?> type = method.getReturnType();

        TreeDescriptionMetadata metadata = TreeDescriptionMetadata.of(
                method.getName() + Casts.LEFT_BRACKET + Casts.RIGHT_BRACKET,
                description.value()
        );

        metadata.setType(type);

        return metadata;
    }

    /**
     * 转换为描述元数据
     *
     * @param field 字段
     *
     * @return 描述元数据集合
     */
    public static TreeDescriptionMetadata convertDescriptionMetadata(Field field) {

        Description description = AnnotationUtils.findAnnotation(field, Description.class);

        if (Objects.isNull(description)) {
            description = AnnotationUtils.findAnnotation(field.getType(), Description.class);
        }

        if (Objects.isNull(description)) {
            return null;
        }

        Class<?> type = field.getType();

        TreeDescriptionMetadata metadata = TreeDescriptionMetadata.of(
                field.getName(),
                description.value()
        );
        metadata.setType(type);

        if (isPrimitive(type)) {
            return metadata;
        }

        Class<?> nextType = type;
        if (Collection.class.isAssignableFrom(type)) {
            nextType = ReflectionUtils.getGenericClass(field, 0);
            if (nextType.isInterface()) {
                return metadata;
            }
        }

        List<Field> fields = ReflectionUtils.findFields(nextType);
        fields
                .stream()
                .map(Casts::convertDescriptionMetadata).filter(Objects::nonNull)
                .peek(f -> f.setParentId(metadata.getId()))
                .forEach(f -> metadata.getChildren().add(f));

        return metadata;
    }

    /**
     * 创建一个新的对象，并将 source 属性内容拷贝到创建的对象中
     *
     * @param source           原数据
     * @param targetClass      新的对象类型
     * @param ignoreProperties 要忽略的属性名称
     * @return 新的对象内容
     */
    public static <T> T of(Object source, Class<T> targetClass, String... ignoreProperties) {

        T result = null;

        if (Objects.nonNull(targetClass)) {
            result = ClassUtils.newInstance(targetClass);
        }

        if (Objects.nonNull(source)) {
            BeanUtils.copyProperties(source, result, ignoreProperties);
        }

        return result;
    }

    public static <T> T ofMap(Map<String,Object> source, Class<T> targetClass, String... ignoreProperties) {
        T result = ClassUtils.newInstance(targetClass);

        for (Map.Entry<String, Object> entry : source.entrySet()) {
            if (ArrayUtils.contains(ignoreProperties, entry.getKey())) {
                continue;
            }
            Field field = ReflectionUtils.findFiled(result, entry.getKey());
            if (Objects.isNull(field)) {
                continue;
            }

            ReflectionUtils.setFieldValue(result, field.getName(), cast(entry.getValue(), field.getType()));
        }

        return result;
    }


    public static List<Field> getIgnoreField(Class<?> targetClass) {
        List<Field> fields = new LinkedList<>();
        for (Field o : targetClass.getDeclaredFields()) {
            IgnoreField ignoreField = o.getAnnotation(IgnoreField.class);
            if (Objects.isNull(ignoreField)) {
                continue;
            }
            fields.add(o);
        }
        return fields;
    }

    public static boolean isPrimitive(Class<?> value) {
        return (Boolean.class.isAssignableFrom(value) ||
                Byte.class.isAssignableFrom(value) ||
                Character.class.isAssignableFrom(value) ||
                Short.class.isAssignableFrom(value) ||
                Integer.class.isAssignableFrom(value) ||
                Long.class.isAssignableFrom(value) ||
                Float.class.isAssignableFrom(value) ||
                Double.class.isAssignableFrom(value)) ||
                String.class.isAssignableFrom(value);
    }

    /**
     * 将蛇转驼峰
     *
     * @param snakeCase 蛇命名
     *
     * @return 驼峰命名名称
     */
    public static String castSnakeCaseToCamelCase(String snakeCase) {
        // 首先将字段名按下划线分割成单词
        String[] words = snakeCase.split(UNDERSCORE);

        // 如果只有一个单词，无需转换，直接返回
        if (words.length == 1) {
            return words[0];
        }

        // 从第二个单词开始，将首字母大写
        StringBuilder result = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            result.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
        }

        return result.toString();
    }

    /**
     * 将驼峰转蛇
     *
     * @param camelCase 驼峰命名
     *
     * @return 驼峰命名名称
     */
    public static String castCamelCaseToSnakeCase(String camelCase) {
        StringBuilder snakeCase = new StringBuilder();
        boolean isFirst = true;

        for (char c : camelCase.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (!isFirst) {
                    snakeCase.append(UNDERSCORE);
                }
                snakeCase.append(Character.toLowerCase(c));
            } else {
                snakeCase.append(c);
            }
            isFirst = false;
        }

        return snakeCase.toString();
    }


    public static String dateTimeFormat(Object date) {
        return dateFormat(date, DEFAULT_DATE_FORMATTER_PATTERN + StringUtils.SPACE + DEFAULT_TIME_FORMATTER_PATTERN);
    }

    public static String dateFormat(Object date) {
        return dateFormat(date, DEFAULT_DATE_FORMATTER_PATTERN);
    }

    public static String dateFormat(Object date, String pattern) {
        Assert.notNull(date, "date must not be null");

        if (ChronoLocalDateTime.class.isAssignableFrom(date.getClass())) {
            ChronoLocalDateTime<?> time = Casts.cast(date);
            return time.format(DateTimeFormatter.ofPattern(pattern));
        } else if (Date.class.isAssignableFrom(date.getClass())) {
            Date d = Casts.cast(date);
            return new SimpleDateFormat(pattern).format(d);
        } else if (Instant.class.isAssignableFrom(date.getClass())) {
            Instant i = Casts.cast(date);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(i, ZoneId.systemDefault());
            return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
        } else if (Long.class.isAssignableFrom(date.getClass())) {
            Long time = Casts.cast(date);
            return dateFormat(new Date(time), pattern);
        } else if (BigDecimal.class.isAssignableFrom(date.getClass())) {
            BigDecimal time = Casts.cast(date);
            return dateFormat(Casts.convertValue(time, Instant.class), pattern);
        } else {
            throw new SystemException("不支持对象 [" + date.getClass().getName() + "] 的日期类型转换");
        }
    }

    /**
     * 获取维度值
     *
     * @param count 维度数量
     * @param args 取模 hash 参数
     *
     * @return 维度值
     */
    public static String getDimension(Integer count, Object... args) {
        return String.format("%0" + String.valueOf(count).length() + "d", Math.abs(Objects.hash(args) % count + 1));
    }

    public static Map<String, Object> ignoreObjectFieldToMap(Object source, List<String> properties) {
        DocumentContext documentContext = createDocumentContext(source, Option.DEFAULT_PATH_LEAF_TO_NULL);
        properties.forEach(documentContext::delete);
        return documentContext.json();
    }

    public static Map<String, Object> desensitizeObjectFieldToMap(Object source, List<String> properties) {
        DocumentContext documentContext = createDocumentContext(source, Option.ALWAYS_RETURN_LIST);
        for (String property : properties) {
            Object value = documentContext.read(property);
            if (Objects.isNull(value)) {
                continue;
            }
            if (value instanceof JSONArray array) {
                Configuration pathConfig = Configuration.builder()
                        .options(Option.SUPPRESS_EXCEPTIONS, Option.AS_PATH_LIST)
                        .build();
                List<String> paths = JsonPath.using(pathConfig).parse(documentContext.jsonString()).read(property);
                if(CollectionUtils.isEmpty(paths)) {
                    continue;
                }
                for (int i = 0; i < array.size(); i++) {
                    Object item = array.get(i);
                    if (Objects.isNull(item)) {
                        continue;
                    }
                    String desensitizeValue = Objects.toString(item, StringUtils.EMPTY);
                    documentContext.set(paths.get(i),DesensitizeSerializer.desensitize(desensitizeValue));
                }
            } else {
                documentContext.set(property, DesensitizeSerializer.desensitize(value.toString()));
            }
        }
        return documentContext.json();
    }

    private static DocumentContext createDocumentContext(Object source, Option option) {
        JsonNode rootNode = objectMapper.valueToTree(source);
        JsonNode filteredNode = rootNode.deepCopy();

        Configuration conf = Configuration.builder()
                .options(Option.SUPPRESS_EXCEPTIONS, option)
                .build();

        return JsonPath.using(conf).parse(filteredNode.toString());
    }

    public static String incrementVersionRevision(String version) {
        String[] parts = version.split(VERSION_SPLIT_REGEX);
        int revision = Integer.parseInt(parts[2]) + 1;

        // 修订号进位逻辑
        if (revision >= 100) { // 假设修订号上限为99
            revision = 0;
            int minor = Integer.parseInt(parts[1]) + 1;
            if (minor >= 10) { // 次版本号上限为9
                minor = 0;
                int major = Integer.parseInt(parts[0]) + 1;
                return major + DOT + minor + DOT + BigDecimal.ZERO;
            }
            parts[1] = String.valueOf(minor);
        }
        parts[2] = String.valueOf(revision);
        return String.join(DOT, parts);
    }

    /**
     * 字符串转整型
     *
     * @param string 字符串
     *
     * @return 整型
     */
    public static BigInteger stringToNumber(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return new BigInteger(1, bytes); // 保持正数表示
    }

    /**
     * 整型转字符串
     *
     * @param number 整形术
     *
     * @return 字符串
     */
    public static String numberToString(BigInteger number) {
        byte[] bytes = number.toByteArray();
        // 处理可能的符号字节
        int start = (bytes.length > 0 && bytes[0] == 0) ? 1 : 0;
        return new String(bytes, start, bytes.length - start, StandardCharsets.UTF_8);
    }

}
