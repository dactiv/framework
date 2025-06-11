package com.github.dactiv.framework.commons.test.enitty;

import com.github.dactiv.framework.commons.annotation.Description;
import com.github.dactiv.framework.commons.annotation.JsonCollectionGenericType;
import com.github.dactiv.framework.commons.enumerate.support.ExecuteStatus;
import com.github.dactiv.framework.commons.enumerate.support.YesOrNo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Description("键值对数据")
public class NamValueEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2901080212503878142L;

    @Description("是或否")
    private YesOrNo yesOrNo;

    @JsonCollectionGenericType(ExecuteStatus.class)
    private List<ExecuteStatus> executeStatuses;

    public NamValueEntity() {
    }

    public YesOrNo getYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(YesOrNo yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    public List<ExecuteStatus> getExecuteStatuses() {
        return executeStatuses;
    }

    public void setExecuteStatuses(List<ExecuteStatus> executeStatuses) {
        this.executeStatuses = executeStatuses;
    }
}
