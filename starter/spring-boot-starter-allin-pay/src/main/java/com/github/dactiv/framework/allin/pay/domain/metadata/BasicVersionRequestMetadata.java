package com.github.dactiv.framework.allin.pay.domain.metadata;

import java.io.Serial;

public class BasicVersionRequestMetadata extends BasicRequestMetadata{

    @Serial
    private static final long serialVersionUID = -5865907332619198826L;

    private String version = "v1.0";

    public BasicVersionRequestMetadata() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
