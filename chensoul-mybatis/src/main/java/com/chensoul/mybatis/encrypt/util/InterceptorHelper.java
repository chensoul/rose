package com.chensoul.mybatis.encrypt.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chensoul.mybatis.encrypt.FieldSetProperty;
import com.chensoul.mybatis.encrypt.IEncryptor;
import com.chensoul.mybatis.encrypt.annotation.FieldEncrypt;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@Slf4j
public class InterceptorHelper {

	private static Map<Class<? extends IEncryptor>, IEncryptor> encryptorMap;

	public static Object encrypt(Invocation invocation, IEncryptor encryptor, String password) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement) args[0];

		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
		if (SqlCommandType.UPDATE == sqlCommandType || SqlCommandType.INSERT == sqlCommandType
			|| SqlCommandType.SELECT == sqlCommandType) {
			Object paramMap = args[1];
			Configuration configuration = mappedStatement.getConfiguration();
			if (paramMap instanceof Map) {
				for (Map.Entry entry : ((Map<?, ?>) paramMap).entrySet()) {
					if (entry.getValue() == null) {
						continue;
					}
					if (((String) entry.getKey()).startsWith("param")) {
						continue;
					}

					if (entry.getValue() instanceof ArrayList) {
						for (Object var : (ArrayList) entry.getValue()) {
							if (encryptValue(configuration, encryptor, password, var)) {
							}
						}
					} else if (entry.getValue() instanceof QueryWrapper) {
						Object entity = ((QueryWrapper<?>) entry.getValue()).getEntity();
						if (entity == null) {
							continue;
						}
						encryptValue(configuration, encryptor, password, entity);
					} else if (!encryptValue(configuration, encryptor, password, entry.getValue())) {
						continue;
					}
					return invocation.proceed();
				}
			} else {
				if (paramMap != null) {
					encryptValue(configuration, encryptor, password, paramMap);
				}
			}
		}
		return invocation.proceed();
	}

	public static boolean encryptValue(Configuration configuration, IEncryptor encryptor, String password,
									   Object object) {
		return FieldSetPropertyHelper.foreachValue(configuration, object, (metaObject, fieldSetProperty) -> {
			FieldEncrypt fieldEncrypt = fieldSetProperty.getFieldEncrypt();
			if (null != fieldEncrypt) {
				Object objectValue = metaObject.getValue(fieldSetProperty.getFieldName());
				if (null != objectValue) {
					try {
						String value = getEncryptor(encryptor, fieldEncrypt.encryptor())
							.encrypt(fieldEncrypt.algorithm(), password, (String) objectValue, null);
						metaObject.setValue(fieldSetProperty.getFieldName(), value);
					} catch (Exception e) {
						log.error("field encrypt", e.getMessage());
					}
				}
			}
		});
	}

	public static IEncryptor getEncryptor(IEncryptor encryptor, Class<? extends IEncryptor> customEncryptor) {
		IEncryptor result = encryptor;
		if (IEncryptor.class != customEncryptor) {
			if (null == encryptorMap) {
				encryptorMap = new HashMap();
			}

			result = encryptorMap.get(customEncryptor);
			if (null == result) {
				try {
					result = customEncryptor.newInstance();
					encryptorMap.put(customEncryptor, result);
				} catch (Exception var4) {
					log.error("fieldEncrypt encryptor newInstance error", var4);
				}
			}
		}
		return result;
	}

	public static Object decrypt(Invocation invocation, BiConsumer<MetaObject, FieldSetProperty> consumer)
		throws Throwable {
		List result = (List) invocation.proceed();
		if (result.isEmpty()) {
			return result;
		} else {
			DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
			Field field = defaultResultSetHandler.getClass().getDeclaredField("mappedStatement");
			field.setAccessible(true);
			MappedStatement mappedStatement = (MappedStatement) field.get(defaultResultSetHandler);
			Configuration configuration = mappedStatement.getConfiguration();
			Iterator iterator = result.iterator();

			while (iterator.hasNext()) {
				Object value = iterator.next();
				if (null != value && !FieldSetPropertyHelper.foreachValue(configuration, value, consumer)) {
					break;
				}
			}
			return result;
		}
	}

}
