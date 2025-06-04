package com.github.dactiv.framework.fasc.res.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseBean;

import java.util.List;

public class SignTaskGetCerInfoRes extends BaseBean {
    private List<CerInfoActor> actors;

    public List<CerInfoActor> getActors() {
        return actors;
    }

    public void setActors(List<CerInfoActor> actors) {
        this.actors = actors;
    }
}
