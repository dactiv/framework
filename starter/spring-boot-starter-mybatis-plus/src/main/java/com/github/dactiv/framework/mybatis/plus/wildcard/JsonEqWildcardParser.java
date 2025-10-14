package com.github.dactiv.framework.mybatis.plus.wildcard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.spring.web.query.Property;
import com.github.dactiv.framework.spring.web.query.generator.WildcardParser;
import org.apache.commons.lang3.StringUtils;

/**
 * json 格式的等于查询通配符实现
 *
 * @author maurice.chen
 */
public class JsonEqWildcardParser<T> implements WildcardParser<QueryWrapper<T>> {

    private final static String DEFAULT_WILDCARD_VALUE = "jeq";

    private final static String DEFAULT_WILDCARD_NAME = "Json 数据格式值等于";

    @Override
    public void structure(Property property, QueryWrapper<T> query) {
        String path = StringUtils.substringAfter(property.getPropertyName(), Casts.DOT);
        String field = StringUtils.substringBefore(property.getPropertyName(), Casts.DOT);
        query.apply(property.splicePropertyName(field) + "->'$." + path + "' = {0}", property.getValue());
    }

    @Override
    public boolean isSupport(String condition) {
        return DEFAULT_WILDCARD_VALUE.equals(condition);
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
