package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

public class SignFieldMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -4424181914495829580L;

    private String fieldDocId;
    private String fieldId;
    private String fieldName;
    private Long sealId;
    private Boolean moveable;

    public SignFieldMetadata() {
    }

    public Boolean getMoveable() {
        return this.moveable;
    }

    public void setMoveable(Boolean moveable) {
        this.moveable = moveable;
    }

    public String getFieldDocId() {
        return this.fieldDocId;
    }

    public void setFieldDocId(String fieldDocId) {
        this.fieldDocId = fieldDocId;
    }

    public String getFieldId() {
        return this.fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Long getSealId() {
        return this.sealId;
    }

    public void setSealId(Long sealId) {
        this.sealId = sealId;
    }

}
