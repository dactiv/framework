package com.github.dactiv.framework.mybatis.plus.wildcard;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.spring.web.query.Property;
import org.apache.commons.lang3.StringUtils;

/**
 * json 格式的包含查询通配符实现
 *
 * @author maurice.chen
 */
public class JsonContainsWildcardParser<T> extends AbstractJsonFunctionWildcardParser<T> {

    private final static String DEFAULT_WILDCARD_VALUE = "jin";

    private final static String DEFAULT_WILDCARD_NAME = "Json 数据格式值包含";

    @Override
    public boolean isSupport(String condition) {
        return DEFAULT_WILDCARD_VALUE.equals(condition);
    }

    @Override
    protected String getExpression(Property property, Integer index) {

        if (StringUtils.contains(property.getPropertyName(), Casts.DOT)) {
            String path = StringUtils.substringAfter(property.getPropertyName(), Casts.DOT);
            String field = StringUtils.substringBefore(property.getPropertyName(), Casts.DOT);
            return "JSON_CONTAINS(" + property.splicePropertyName(field) + "->'$[*]." + path  + "', JSON_ARRAY({" + index + "}), '$')";
        }

        return "JSON_CONTAINS(" + property.getFinalPropertyName() + ", JSON_ARRAY({" + index + "}))";
    }

    @Override
    public String getName() {
        return DEFAULT_WILDCARD_NAME;
    }

    @Override
    public String getValue() {
        return DEFAULT_WILDCARD_VALUE;
    }
}
