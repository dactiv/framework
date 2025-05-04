package com.github.dactiv.framework.fadada.domain.body.task;

import com.github.dactiv.framework.fadada.domain.metadata.task.WeChatMiniProgramMetadata;

import java.io.Serial;
import java.io.Serializable;

public class SignTaskActorGetUrlResponseBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -7272447586136147883L;

    private String actorSignTaskUrl;
    private String actorSignTaskEmbedUrl;
    private WeChatMiniProgramMetadata actorSignTaskMiniAppInfo;

    public SignTaskActorGetUrlResponseBody() {

    }

    public String getActorSignTaskUrl() {
        return actorSignTaskUrl;
    }

    public void setActorSignTaskUrl(String actorSignTaskUrl) {
        this.actorSignTaskUrl = actorSignTaskUrl;
    }

    public String getActorSignTaskEmbedUrl() {
        return actorSignTaskEmbedUrl;
    }

    public void setActorSignTaskEmbedUrl(String actorSignTaskEmbedUrl) {
        this.actorSignTaskEmbedUrl = actorSignTaskEmbedUrl;
    }

    public WeChatMiniProgramMetadata getActorSignTaskMiniAppInfo() {
        return actorSignTaskMiniAppInfo;
    }

    public void setActorSignTaskMiniAppInfo(WeChatMiniProgramMetadata actorSignTaskMiniAppInfo) {
        this.actorSignTaskMiniAppInfo = actorSignTaskMiniAppInfo;
    }
}
