package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;

public class WatermarkMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -2176464750224327863L;

    private String type;
    private String content;
    private String picFileId;
    private String picBase64;
    private String picWidth;
    private String picHeight;
    private Integer fontSize;
    private String fontColor;
    private Integer rotation;
    private Integer transparency;
    private String position;
    private String density;

    public WatermarkMetadata() {
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicFileId() {
        return this.picFileId;
    }

    public void setPicFileId(String picFileId) {
        this.picFileId = picFileId;
    }

    public String getPicBase64() {
        return this.picBase64;
    }

    public void setPicBase64(String picBase64) {
        this.picBase64 = picBase64;
    }

    public String getPicWidth() {
        return this.picWidth;
    }

    public void setPicWidth(String picWidth) {
        this.picWidth = picWidth;
    }

    public String getPicHeight() {
        return this.picHeight;
    }

    public void setPicHeight(String picHeight) {
        this.picHeight = picHeight;
    }

    public Integer getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontColor() {
        return this.fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public Integer getRotation() {
        return this.rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Integer getTransparency() {
        return this.transparency;
    }

    public void setTransparency(Integer transparency) {
        this.transparency = transparency;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDensity() {
        return this.density;
    }

    public void setDensity(String density) {
        this.density = density;
    }
}
