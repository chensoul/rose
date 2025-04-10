package com.chensoul.excel.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelExplicitConstraint {

	Class source();

}
