package com.chensoul.core.spring;

import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.reflect.Constructor;

public class ClassUtils extends org.springframework.util.ClassUtils {

	private static final ConcurrentReferenceHashMap<Class<?>, Constructor<?>[]> CONSTRUCTORS_CACHE = new ConcurrentReferenceHashMap<>(
		16, ConcurrentReferenceHashMap.ReferenceType.WEAK);

	public static Object getPrimitiveDefaultValue(Class<?> clazz) {
		if (long.class == clazz) {
			return 0L;
		} else if (int.class == clazz) {
			return 0;
		} else if (short.class == clazz) {
			return (short) 0;
		} else if (char.class == clazz) {
			return (char) 0;
		} else if (byte.class == clazz) {
			return (byte) 0;
		} else if (double.class == clazz) {
			return 0D;
		} else if (float.class == clazz) {
			return 0f;
		} else if (boolean.class == clazz) {
			return false;
		}
		return null;
	}

	public static Object[] getDefaultValues(Class<?>... classes) {
		final Object[] values = new Object[classes.length];
		for (int i = 0; i < classes.length; i++) {
			values[i] = getDefaultValue(classes[i]);
		}
		return values;
	}

	public static Object getDefaultValue(Class<?> clazz) {
		// 原始类型
		if (clazz.isPrimitive()) {
			return getPrimitiveDefaultValue(clazz);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T>[] getConstructors(Class<T> beanClass) throws SecurityException {
		Assert.notNull(beanClass);
		return (Constructor<T>[]) CONSTRUCTORS_CACHE.computeIfAbsent(beanClass,
			aClass -> getConstructorsDirectly(beanClass));
	}

	public static Constructor<?>[] getConstructorsDirectly(Class<?> beanClass) throws SecurityException {
		return beanClass.getDeclaredConstructors();
	}

}
