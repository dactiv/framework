package com.github.dactiv.framework.fadada.domain.metadata;

import java.util.Map;

public class CustomNotifyMetadata {

    private String serviceCenterName;
    private String customUrl;
    private Map<String, String> varMap;

    public CustomNotifyMetadata() {
    }

    public Map<String, String> getVarMap() {
        return this.varMap;
    }

    public void setVarMap(Map<String, String> varMap) {
        this.varMap = varMap;
    }

    public String getServiceCenterName() {
        return this.serviceCenterName;
    }

    public void setServiceCenterName(String serviceCenterName) {
        this.serviceCenterName = serviceCenterName;
    }

    public String getCustomUrl() {
        return this.customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }
}
