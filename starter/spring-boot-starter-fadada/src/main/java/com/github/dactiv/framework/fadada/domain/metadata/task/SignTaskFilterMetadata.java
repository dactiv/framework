package com.github.dactiv.framework.fadada.domain.metadata.task;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SignTaskFilterMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -5863543279139243225L;

    private String signTaskSubject;
    private List<String> signTaskStatus;
    private String transReferenceId;
    private String startTimeFrom;
    private String startTimeTo;
    private String finishTimeFrom;
    private String finishTimeTo;
    private String expiresTimeFrom;
    private String expiresTimeTo;
    private Long businessTypeId;
    private String businessCode;
    private String actorInfo;
    private String catalogId;

    public SignTaskFilterMetadata() {
    }

    public String getSignTaskSubject() {
        return signTaskSubject;
    }

    public void setSignTaskSubject(String signTaskSubject) {
        this.signTaskSubject = signTaskSubject;
    }

    public List<String> getSignTaskStatus() {
        return signTaskStatus;
    }

    public void setSignTaskStatus(List<String> signTaskStatus) {
        this.signTaskStatus = signTaskStatus;
    }

    public String getTransReferenceId() {
        return transReferenceId;
    }

    public void setTransReferenceId(String transReferenceId) {
        this.transReferenceId = transReferenceId;
    }

    public String getStartTimeFrom() {
        return startTimeFrom;
    }

    public void setStartTimeFrom(String startTimeFrom) {
        this.startTimeFrom = startTimeFrom;
    }

    public String getStartTimeTo() {
        return startTimeTo;
    }

    public void setStartTimeTo(String startTimeTo) {
        this.startTimeTo = startTimeTo;
    }

    public String getFinishTimeFrom() {
        return finishTimeFrom;
    }

    public void setFinishTimeFrom(String finishTimeFrom) {
        this.finishTimeFrom = finishTimeFrom;
    }

    public String getFinishTimeTo() {
        return finishTimeTo;
    }

    public void setFinishTimeTo(String finishTimeTo) {
        this.finishTimeTo = finishTimeTo;
    }

    public String getExpiresTimeFrom() {
        return expiresTimeFrom;
    }

    public void setExpiresTimeFrom(String expiresTimeFrom) {
        this.expiresTimeFrom = expiresTimeFrom;
    }

    public String getExpiresTimeTo() {
        return expiresTimeTo;
    }

    public void setExpiresTimeTo(String expiresTimeTo) {
        this.expiresTimeTo = expiresTimeTo;
    }

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getActorInfo() {
        return actorInfo;
    }

    public void setActorInfo(String actorInfo) {
        this.actorInfo = actorInfo;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }
}
