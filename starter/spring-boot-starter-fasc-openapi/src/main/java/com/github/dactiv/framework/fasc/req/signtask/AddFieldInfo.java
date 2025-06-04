package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;
import com.github.dactiv.framework.fasc.bean.common.Field;

import java.util.List;

/**
 * @author Fadada
 * @date 2021/12/6 11:30:58
 */
public class AddFieldInfo extends BaseBean {
    private String docId;
    private List<Field> docFields;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public List<Field> getDocFields() {
        return docFields;
    }

    public void setDocFields(List<Field> docFields) {
        this.docFields = docFields;
    }
}
