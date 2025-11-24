package com.github.dactiv.framework.commons.id;

import com.github.dactiv.framework.commons.CacheProperties;
import com.github.dactiv.framework.commons.ReflectionUtils;
import com.github.dactiv.framework.commons.enumerate.NameEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Objects;

/**
 * 带名称的 id 元数据
 *
 * @author maurice.chen
 */
public class IdNameMetadata extends IdEntity<String> {

    public static final String NAME_FIELD_NAME = "name";

    @Serial
    private static final long serialVersionUID = -4127420686530448230L;

    /**
     * 名称
     */
    private String name;

    public static IdNameMetadata of(String id, String name) {
        IdNameMetadata result = new IdNameMetadata();
        result.setId(id);
        result.setName(name);
        return result;
    }

    public static TypeIdNameMetadata ofPrincipalString(String principalString) {

        String[] split = StringUtils.splitByWholeSeparator(principalString, CacheProperties.DEFAULT_SEPARATOR);
        Assert.isTrue(split.length >= 2, "Invalid user string");

        TypeIdNameMetadata result = new TypeIdNameMetadata();

        result.setType(split[0]);
        result.setId(split[1]);

        if(split.length > 2) {
            result.setName(split[2]);
        }

        return result;
    }

    public static IdNameMetadata of(BasicIdentification<String> source) {
        return of(source, NameEnum.FIELD_NAME);
    }

    public static IdNameMetadata of(BasicIdentification<String> source, String nameProperty) {
        IdNameMetadata result = new IdNameMetadata();
        result.setId(source.getId());

        Object nameValue = ReflectionUtils.getReadProperty(source, nameProperty);

        if (Objects.isNull(nameValue)) {
            nameValue = ReflectionUtils.getFieldValue(source, nameProperty);
        }

        if (Objects.nonNull(nameValue)) {
            result.setName(nameValue.toString());
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        IdNameMetadata that = (IdNameMetadata) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
