package com.chensoul.mybatis.encrypt.interceptor;

import com.chensoul.mybatis.encrypt.FieldSetProperty;
import com.chensoul.mybatis.encrypt.IEncryptor;
import com.chensoul.mybatis.encrypt.IFieldBinder;
import com.chensoul.mybatis.encrypt.annotation.FieldBind;
import com.chensoul.mybatis.encrypt.annotation.FieldEncrypt;
import com.chensoul.mybatis.encrypt.util.FieldSetPropertyHelper;
import com.chensoul.mybatis.encrypt.util.InterceptorHelper;
import java.sql.Statement;
import java.util.Properties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Intercepts({
    @Signature(
            type = ResultSetHandler.class,
            method = "handleResultSets",
            args = {Statement.class})
})
@Slf4j
@Data
public class FieldDecryptInterceptor implements Interceptor {

    private IEncryptor encryptor;

    private IFieldBinder fieldBinder;

    private String password;

    public FieldDecryptInterceptor(IEncryptor encryptor, IFieldBinder fieldBinder, String password) {
        this.encryptor = encryptor;
        this.fieldBinder = fieldBinder;
        this.password = password;

        FieldSetPropertyHelper.init(null != encryptor, null != fieldBinder);
    }

    public Object intercept(Invocation invocation) throws Throwable {
        return InterceptorHelper.decrypt(invocation, (metaObject, fieldSetProperty) -> {
            decrypt(this.encryptor, this.fieldBinder, this.password, metaObject, fieldSetProperty);
        });
    }

    public void decrypt(
            IEncryptor encryptor,
            IFieldBinder fieldBinder,
            String password,
            MetaObject metaObject,
            FieldSetProperty fieldSetProperty) {
        String fieldName = fieldSetProperty.getFieldName();
        Object value = metaObject.getValue(fieldName);
        if (null != value) {
            if (null != encryptor && value instanceof String) {
                try {
                    FieldEncrypt fieldEncrypt = fieldSetProperty.getFieldEncrypt();
                    if (null != fieldEncrypt) {
                        value = InterceptorHelper.getEncryptor(encryptor, fieldEncrypt.encryptor())
                                .decrypt(fieldEncrypt.algorithm(), password, (String) value, null);
                    }
                } catch (Exception e) {
                    log.error("field decrypt", e);
                }
            }

            if (null != fieldBinder) {
                FieldBind fieldBind = fieldSetProperty.getFieldBind();
                if (null != fieldBind) {
                    fieldBinder.setMetaObject(fieldBind, value, metaObject);
                }
            }
            metaObject.setValue(fieldName, value);
        }
    }

    public Object plugin(Object var1) {
        return var1 instanceof ResultSetHandler ? Plugin.wrap(var1, this) : var1;
    }

    public void setProperties(Properties properties) {}
}
