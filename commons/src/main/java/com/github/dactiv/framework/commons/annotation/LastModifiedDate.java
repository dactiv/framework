package com.github.dactiv.framework.commons.annotation;

import java.lang.annotation.*;

/**
 * 最后更新时间注解
 *
 * @author maurice.chen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LastModifiedDate {
}
