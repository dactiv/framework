package com.github.dactiv.framework.fadada.domain.metadata;

import java.io.Serial;
import java.io.Serializable;

public class PositionMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -5513381408557686661L;

    private Integer positionPageNo;
    private String positionX;
    private String positionY;

    public PositionMetadata() {
    }

    public Integer getPositionPageNo() {
        return positionPageNo;
    }

    public void setPositionPageNo(Integer positionPageNo) {
        this.positionPageNo = positionPageNo;
    }

    public String getPositionX() {
        return positionX;
    }

    public void setPositionX(String positionX) {
        this.positionX = positionX;
    }

    public String getPositionY() {
        return positionY;
    }

    public void setPositionY(String positionY) {
        this.positionY = positionY;
    }
}
