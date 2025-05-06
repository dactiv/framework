package com.github.dactiv.framework.fadada.domain.metadata.task;

import com.github.dactiv.framework.fadada.domain.metadata.PositionMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.SignFieldMetadata;

import java.io.Serial;

public class SignTaskFieldMetadata extends SignFieldMetadata {
    @Serial
    private static final long serialVersionUID = -7153767521474912771L;

    private String signFieldStatus;
    private PositionMetadata position;
    private String signRemark;
    private Long sealId;
    private String categoryType;

    public SignTaskFieldMetadata() {
    }

    public String getSignFieldStatus() {
        return signFieldStatus;
    }

    public void setSignFieldStatus(String signFieldStatus) {
        this.signFieldStatus = signFieldStatus;
    }

    public PositionMetadata getPosition() {
        return position;
    }

    public void setPosition(PositionMetadata position) {
        this.position = position;
    }

    public String getSignRemark() {
        return signRemark;
    }

    public void setSignRemark(String signRemark) {
        this.signRemark = signRemark;
    }

    public Long getSealId() {
        return sealId;
    }

    public void setSealId(Long sealId) {
        this.sealId = sealId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
