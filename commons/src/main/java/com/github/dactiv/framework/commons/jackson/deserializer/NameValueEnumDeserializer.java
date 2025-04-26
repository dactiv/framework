package com.github.dactiv.framework.commons.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.annotation.JsonCollectionGenericType;
import com.github.dactiv.framework.commons.enumerate.NameValueEnum;
import com.github.dactiv.framework.commons.enumerate.ValueEnum;
import com.github.dactiv.framework.commons.exception.SystemException;

import java.io.IOException;
import java.util.*;

/**
 * 值于名称枚举的反序列化实现
 *
 *
 * @author maurice.chen
 */
@SuppressWarnings("rawtypes")
public class NameValueEnumDeserializer<T extends NameValueEnum> extends JsonDeserializer<T> implements ContextualDeserializer {

    private Class<?> targetType;

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode jsonNode = p.getCodec().readTree(p);

        String nodeValue = getNodeValue(jsonNode);
        List<NameValueEnum> valueEnums = Arrays
                .stream(targetType.getEnumConstants())
                .map(v -> Casts.cast(v, NameValueEnum.class))
                .toList();

        Optional<NameValueEnum> optional = valueEnums
                .stream()
                .filter(v -> v.toString().equals(nodeValue))
                .findFirst();

        if (optional.isEmpty()) {
            optional = valueEnums
                    .stream()
                    .filter(v -> v.getName().equals(nodeValue))
                    .findFirst();
        }

        if (optional.isEmpty()) {

            optional = valueEnums
                    .stream()
                    .filter(v -> v.getValue().toString().equals(nodeValue))
                    .findFirst();
        }

        NameValueEnum result = optional
                .orElseThrow(() -> new SystemException("在类型 [" + targetType + "] 枚举里找不到值为 [" + nodeValue + "] 的类型"));

        return Casts.cast(result);
    }

    /**
     * 获取 json node 值
     *
     * @param jsonNode json node
     *
     * @return 实际值
     */
    public static String getNodeValue(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            return jsonNode.get(ValueEnum.FIELD_NAME).asText();
        }

        return Objects.isNull(jsonNode.textValue()) ? jsonNode.toString() : jsonNode.textValue();
    }

    public static Class<?> getCurrentTargetType(DeserializationContext ctxt, BeanProperty property) {
        Class<?> targetType = property.getType().getRawClass();
        if (Collection.class.isAssignableFrom(targetType)) {
            JsonCollectionGenericType genericType = property.getAnnotation(JsonCollectionGenericType.class);
            if (Objects.nonNull(genericType)) {
                targetType = genericType.value();
            }
        }
        return targetType;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        this.targetType = getCurrentTargetType(ctxt, property);
        return this;
    }
}
