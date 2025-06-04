package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;
import com.github.dactiv.framework.fasc.bean.common.Actor;

import java.util.List;

/**
 * @author Fadada
 * 2021/9/13 13:38:56
 */
public class CreateWithTemplateFillActorInfo extends BaseBean {
    private Actor fillActor;
    private List<CreateWithTemplateFieldValueInfo> actorFields;

    public Actor getFillActor() {
        return fillActor;
    }

    public void setFillActor(Actor fillActor) {
        this.fillActor = fillActor;
    }

    public List<CreateWithTemplateFieldValueInfo> getActorFields() {
        return actorFields;
    }

    public void setActorFields(List<CreateWithTemplateFieldValueInfo> actorFields) {
        this.actorFields = actorFields;
    }
}

