package com.github.dactiv.framework.commons.id;

import java.io.Serial;
import java.util.Objects;

/**
 * @author maurice.chen
 */
public class TypeIdNameMetadata extends IdNameMetadata {

    public static final String TYPE_FIELD_NAME = "type";

    @Serial
    private static final long serialVersionUID = -5958623464505187650L;

    /**
     * 类型
     */
    private String type;

    public TypeIdNameMetadata() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static TypeIdNameMetadata of(String id, String name, String type) {
        TypeIdNameMetadata result = new TypeIdNameMetadata();

        result.setName(name);
        result.setId(id);
        result.setType(type);

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
        TypeIdNameMetadata that = (TypeIdNameMetadata) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
