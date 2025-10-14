package com.github.dactiv.framework.mybatis.plus.wildcard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.spring.web.query.Property;
import org.apache.commons.lang3.StringUtils;

/**
 * json 查询的返回所有匹配项的路径通配符解析器实现
 *
 * @author maurice.chen
 *
 * @param <T>
 */
public class JsonSearchAllWildcardParser<T> extends AbstractJsonFunctionWildcardParser<T> {

    private final static String DEFAULT_WILDCARD_VALUE = "jsa";

    private final static String DEFAULT_WILDCARD_NAME = "Json 数据格式 all 查询";

    @Override
    public void structure(Property property, QueryWrapper<T> queryWrapper) {
        LikeWildcardParser.addMatchSymbol(property);
        super.structure(property, queryWrapper);
    }

    /**
     * 获取表达式
     *
     * @param property 属性
     * @param index 值索引
     *
     * @return JSON_CONTAINS 表达式
     */
    @Override
    public String getExpression(Property property, Integer index) {

        if (StringUtils.contains(property.getPropertyName(), Casts.DOT)) {
            String path = StringUtils.substringAfter(property.getPropertyName(), Casts.DOT);
            String field = StringUtils.substringBefore(property.getPropertyName(), Casts.DOT);
            return "JSON_SEARCH(" + property.splicePropertyName(field) + "->'$[*]." + path  + "', 'all', {" + index + "}, '$') IS NOT NULL";
        }

        return "JSON_SEARCH(" + property.getFinalPropertyName() + ", 'all', {" + index + "}) IS NOT NULL";
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
