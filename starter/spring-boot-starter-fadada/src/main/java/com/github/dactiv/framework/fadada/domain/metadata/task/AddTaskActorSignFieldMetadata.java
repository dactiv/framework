package com.github.dactiv.framework.fadada.domain.metadata.task;

import com.github.dactiv.framework.fadada.domain.metadata.SignFieldMetadata;

import java.io.Serial;

public class AddTaskActorSignFieldMetadata extends SignFieldMetadata {

    @Serial
    private static final long serialVersionUID = 3471658172871905364L;

    private String createSerialNo;

    public AddTaskActorSignFieldMetadata() {
    }

    public String getCreateSerialNo() {
        return createSerialNo;
    }

    public void setCreateSerialNo(String createSerialNo) {
        this.createSerialNo = createSerialNo;
    }
}
