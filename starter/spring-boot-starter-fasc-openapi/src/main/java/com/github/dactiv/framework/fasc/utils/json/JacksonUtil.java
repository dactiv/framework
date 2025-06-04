package com.github.dactiv.framework.fasc.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.fasc.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Fadada
 * 2021/9/8 16:28:16
 */
public class JacksonUtil {
    private JacksonUtil() {
    }

    private static final Logger log = LoggerFactory.getLogger(JacksonUtil.class);


    /*private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        *//**
         * 序列化忽略为null属性
         *//*
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // 取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 对于空的对象转json的时候不抛出错误
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // 禁用遇到未知属性抛出异常
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);


        OBJECT_MAPPER.enable(JsonParser.Feature.ALLOW_COMMENTS);
        OBJECT_MAPPER.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        OBJECT_MAPPER.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);

    }*/

    public static String toJson(Object object) throws ApiException {
        // 针对的是转义字符串
        try {
            if (object instanceof String) {
                JsonNode jsonNode = Casts.getObjectMapper().readTree(String.valueOf(object));
                return Casts.getObjectMapper().writeValueAsString(jsonNode);
            } else {
                return Casts.getObjectMapper().writeValueAsString(object);
            }
        } catch (Exception e) {
            log.error("toJson失败：{}", e.getMessage(), e);
            throw new ApiException("toJson失败");
        }
    }

    public static <T> T toJavaBean(String json, final ParameterizedType parameterizedType) throws ApiException {
        if (json == null) {
            return null;
        }
        try {
            return Casts.getObjectMapper().readValue(json, new TypeReference<T>() {
                @Override
                public Type getType() {
                    return parameterizedType;
                }
            });
        } catch (JsonProcessingException e) {
            log.error("toJavaBean失败：{}", e.getMessage(), e);
            throw new ApiException("toJavaBean失败");
        }
    }


    public static <T> T toJavaBean(String json, Class<T> clzz) throws ApiException {
        try {
            return Casts.getObjectMapper().readValue(json, clzz);
        } catch (JsonProcessingException e) {
            log.error("toJavaBean失败：{}", e.getMessage(), e);
            throw new ApiException("toJavaBean失败");
        }
    }

    public static <T> List<T> toList(String json, Class<T> clzz) throws ApiException {
        JavaType javaType = Casts.getObjectMapper().getTypeFactory().constructParametricType(List.class, clzz);
        try {
            return Casts.getObjectMapper().readValue(json, javaType);
        } catch (JsonProcessingException e) {
            log.error("toList失败：{}", e.getMessage(), e);
            throw new ApiException("toList失败");
        }
    }

}
