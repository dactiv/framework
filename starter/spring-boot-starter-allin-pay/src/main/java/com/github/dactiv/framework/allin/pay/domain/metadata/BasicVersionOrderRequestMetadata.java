package com.github.dactiv.framework.allin.pay.domain.metadata;

import java.io.Serial;

public class BasicVersionOrderRequestMetadata extends BasicOrderRequestMetadata {

    @Serial
    private static final long serialVersionUID = -1680898755400650022L;

    private String version;

    public BasicVersionOrderRequestMetadata() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
