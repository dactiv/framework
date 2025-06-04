package com.github.dactiv.framework.fasc.req.signtask;

import com.github.dactiv.framework.fasc.bean.base.BaseReq;

import java.util.List;

public class FillInActorInfoReq extends BaseReq {

    private String signTaskId;
    private List<String> actorIds;


    public String getSignTaskId() {
        return signTaskId;
    }

    public void setSignTaskId(String signTaskId) {
        this.signTaskId = signTaskId;
    }

    public List<String> getActorIds() {
        return actorIds;
    }

    public void setActorIds(List<String> actorIds) {
        this.actorIds = actorIds;
    }
}