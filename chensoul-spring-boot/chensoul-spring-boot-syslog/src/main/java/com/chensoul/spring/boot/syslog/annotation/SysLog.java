package com.chensoul.spring.boot.syslog.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value();

    String expression() default "";

    boolean request() default true;

    boolean response() default false;
}
