package com.github.dactiv.framework.fadada.domain.metadata.doc;

import java.io.Serial;
import java.io.Serializable;

public class AddDocInfoMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -3564203876389075964L;

    private String docId;
    private String docName;
    private String docFileId;
    private String docTemplateId;
}
