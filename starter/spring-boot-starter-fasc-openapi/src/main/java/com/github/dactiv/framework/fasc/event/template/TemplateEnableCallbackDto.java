package com.github.dactiv.framework.fasc.event.template;


/**
 * 模板启用事件
 */
public class TemplateEnableCallbackDto {

    private String eventTime;

    private String openCorpId;

    private String templateId;

    private String type;

    private String clientCorpId;


    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getOpenCorpId() {
        return openCorpId;
    }

    public void setOpenCorpId(String openCorpId) {
        this.openCorpId = openCorpId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClientCorpId() {
        return clientCorpId;
    }

    public void setClientCorpId(String clientCorpId) {
        this.clientCorpId = clientCorpId;
    }
}
