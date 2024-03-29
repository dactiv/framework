package com.github.dactiv.framework.mybatis.plus.wildcard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.dactiv.framework.spring.web.query.Property;
import com.github.dactiv.framework.spring.web.query.generator.WildcardParser;
import org.apache.commons.lang3.BooleanUtils;

/**
 * 等于 null 的通配符实现
 *
 * @author maurice.chen
 */
public class EqnWildcardParser<T> implements WildcardParser<QueryWrapper<T>> {

    private final static String DEFAULT_WILDCARD_VALUE = "eqn";

    private final static String DEFAULT_WILDCARD_NAME = "为 null";

    @Override
    public void structure(Property property, QueryWrapper<T> queryWrapper) {
        if (BooleanUtils.toBoolean(property.getValue().toString())) {
            queryWrapper.isNull(property.getPropertyName());
        }
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
