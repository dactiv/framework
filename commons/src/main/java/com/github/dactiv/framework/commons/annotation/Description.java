package com.github.dactiv.framework.commons.annotation;


import java.lang.annotation.*;

/**
 * 描述注解
 *
 * @author maurice.chen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface Description {

    /**
     * 描述信息
     *
     * @return 泛型类型 class
     */
    String value();
}
