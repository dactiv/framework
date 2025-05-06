package com.github.dactiv.framework.fadada.domain.body.task;

import com.github.dactiv.framework.fadada.domain.metadata.task.SignTaskIdMetadata;

import java.io.Serial;

public class ExtensionSignTaskRequestBody extends SignTaskIdMetadata {

    @Serial
    private static final long serialVersionUID = 6465345282629117505L;

    private String extensionTime;

    public ExtensionSignTaskRequestBody() {
    }

    public ExtensionSignTaskRequestBody(String signTaskId, String extensionTime) {
        super(signTaskId);
        this.extensionTime = extensionTime;
    }

    public String getExtensionTime() {
        return extensionTime;
    }

    public void setExtensionTime(String extensionTime) {
        this.extensionTime = extensionTime;
    }
}
