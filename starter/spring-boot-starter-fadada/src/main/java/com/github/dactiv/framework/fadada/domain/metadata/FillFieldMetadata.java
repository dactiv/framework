package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

public class FillFieldMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = 3296167766664600213L;

    private String fieldDocId;
    private String fieldId;
    private String fieldName;
    private String fieldValue;

    public FillFieldMetadata() {
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

    public String getFieldValue() {
        return this.fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
