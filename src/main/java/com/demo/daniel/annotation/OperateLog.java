package com.demo.daniel.annotation;

import com.demo.daniel.model.entity.LogOperateType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {

    String module() default "";

    String name() default "";

    LogOperateType[] type() default LogOperateType.OTHER;
}
