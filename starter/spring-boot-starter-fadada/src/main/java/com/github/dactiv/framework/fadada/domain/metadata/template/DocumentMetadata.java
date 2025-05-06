package com.github.dactiv.framework.fadada.domain.metadata.template;

import com.fasc.open.api.bean.common.TemplateField;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class DocumentMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -938615905724170770L;
    private Integer docId;
    private String docName;
    private List<TemplateField> docFields;
}
