package com.chensoul.mybatis.encrypt.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface FieldBind {

	String sharding() default "";

	String type();

	String target();

}
