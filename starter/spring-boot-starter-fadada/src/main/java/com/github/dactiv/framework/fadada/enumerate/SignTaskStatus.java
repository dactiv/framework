package com.github.dactiv.framework.fadada.enumerate;

import com.github.dactiv.framework.commons.enumerate.NameValueEnum;

public enum SignTaskStatus implements NameValueEnum<String> {
    TASK_CREATED("task_created","任务创建中 (签署任务创建中，未提交)"),
    FINISH_CREATION("finish_creation","已创建（任务完成创建并在审批中）"),
    FILL_PROGRESS("fill_progress","填写进行中 (签署任务正在进行协同填写流程阶段，必填控件尚未填完)"),
    FILL_COMPLETED("fill_completed","填写已完成 (签署任务文档中所有的必填控件均已填写，但文档尚未定稿)"),
    SIGN_PROGRESS("sign_progress","签署进行中 (签署任务正在进行签署流程阶段)"),
    SIGN_COMPLETED("sign_completed","签署已完成 (签署任务所有参与方均已签署完成)"),
    TASK_FINISHED("task_finished","任务已结束 (签署任务已成功结束)"),
    TASK_TERMINATED("task_terminated","任务异常停止 (签署任务已经因为某种原因而停止运行，如因为某方拒填或拒签、撤销)。"),
    EXPIRED("expired","已逾期"),
    ABOLISHING("abolishing","作废中"),
    REVOKED("revoked","已作废"),
    ;

    SignTaskStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    private final String value;
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
