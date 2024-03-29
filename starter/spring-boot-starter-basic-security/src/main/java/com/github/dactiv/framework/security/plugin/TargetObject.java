package com.github.dactiv.framework.security.plugin;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 目标对象, 用于记录是方法级别的目标还是类级别的目标，等好区分如果构造插件内容。
 *
 * @author maurice.chen
 */
public class TargetObject implements Serializable {

    @Serial
    private static final long serialVersionUID = -3187348696762437870L;

    private final Object target;

    private final List<Method> methodList;

    public TargetObject(Object target, List<Method> methodList) {
        this.methodList = methodList;
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    public List<Method> getMethodList() {
        return methodList;
    }
}
