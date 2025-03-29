package com.github.dactiv.framework.spring.security.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.dactiv.framework.mybatis.plus.baisc.support.IntegerVersionEntity;
import com.github.dactiv.framework.security.audit.AuditPrincipal;

@TableName(value = "tb_operation_data", autoResultMap = true)
public class OperationDataEntity extends IntegerVersionEntity<Integer> implements AuditPrincipal {

    private String name;

    private String owner;

    private String principal;

    public OperationDataEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
