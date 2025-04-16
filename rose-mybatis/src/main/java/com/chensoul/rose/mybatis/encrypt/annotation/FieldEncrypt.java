package com.chensoul.rose.mybatis.encrypt.annotation;

import com.chensoul.rose.mybatis.encrypt.IEncryptor;
import com.chensoul.rose.mybatis.encrypt.util.Algorithm;
import java.lang.annotation.*;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldEncrypt {

    Algorithm algorithm() default Algorithm.BASE64;

    Class<? extends IEncryptor> encryptor() default IEncryptor.class;
}
