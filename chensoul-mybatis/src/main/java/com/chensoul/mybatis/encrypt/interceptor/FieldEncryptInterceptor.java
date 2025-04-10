package com.chensoul.mybatis.encrypt.interceptor;

import com.chensoul.mybatis.encrypt.IEncryptor;
import com.chensoul.mybatis.encrypt.util.InterceptorHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Intercepts({
    @Signature(
            type = Executor.class,
            method = "update",
            args = {MappedStatement.class, Object.class}),
    @Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(
            type = Executor.class,
            method = "query",
            args = {
                MappedStatement.class,
                Object.class,
                RowBounds.class,
                ResultHandler.class,
                CacheKey.class,
                BoundSql.class
            }),
})
@Slf4j
@Data
@AllArgsConstructor
public class FieldEncryptInterceptor implements Interceptor {

    private IEncryptor encryptor;

    private String password;

    public Object intercept(Invocation invocation) throws Throwable {
        return InterceptorHelper.encrypt(invocation, encryptor, password);
    }

    public Object plugin(Object var1) {
        return var1 instanceof Executor ? Plugin.wrap(var1, this) : var1;
    }
}
