package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

/**
 * @author Fadada
 * 2021/10/23 17:35:52
 */
public class AddFillActorFieldInfo extends BaseBean {
    private Integer fieldDocId;
    private String fieldId;
    private String fieldName;
    private String fieldValue;

    public Integer getFieldDocId() {
        return fieldDocId;
    }

    public void setFieldDocId(Integer fieldDocId) {
        this.fieldDocId = fieldDocId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}