package com.github.dactiv.framework.fadada.domain.body.task;

import com.github.dactiv.framework.fadada.domain.body.PageableResponseBody;

import java.io.Serial;
import java.util.List;

public class SignTaskPageResponseBody extends PageableResponseBody {
    @Serial
    private static final long serialVersionUID = 4108117700946676234L;

    private List<SignTaskResponseBody> signTasks;

    public SignTaskPageResponseBody() {
    }

    public List<SignTaskResponseBody> getSignTasks() {
        return signTasks;
    }

    public void setSignTasks(List<SignTaskResponseBody> signTasks) {
        this.signTasks = signTasks;
    }
}
