package com.nest.diamond.common.annos;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogMask {
    // 默认为 ******，你也可以定义不同的脱敏策略
    String value() default "******";
}