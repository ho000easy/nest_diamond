package com.nest.diamond.web.anno;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAccess {
    // 模块名称/操作描述
    String value() default "";

    int dailyLimit() default -1;
}