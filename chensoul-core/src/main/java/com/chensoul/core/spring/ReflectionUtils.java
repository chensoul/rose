package com.chensoul.core.spring;

import static com.chensoul.core.spring.ClassUtils.getConstructors;
import static com.chensoul.core.spring.ClassUtils.getDefaultValues;
import static org.springframework.objenesis.instantiator.util.ClassUtils.newInstance;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Supplier;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

    private static final Map<Class<?>, Supplier<?>> DEFAULT_IMPLEMENTATIONS = new HashMap<>();

    static {
        DEFAULT_IMPLEMENTATIONS.put(Map.class, HashMap::new);
        DEFAULT_IMPLEMENTATIONS.put(Collection.class, ArrayList::new);
        DEFAULT_IMPLEMENTATIONS.put(List.class, ArrayList::new);
        DEFAULT_IMPLEMENTATIONS.put(Set.class, HashSet::new);
    }

    /**
     * 尝试遍历并调用此类的所有构造方法，直到构造成功并返回
     * <p>
     * 对于某些特殊的接口，按照其默认实现实例化，例如： <pre>
     *     Map       -> HashMap
     *     Collection -> ArrayList
     *     List      -> ArrayList
     *     Set       -> HashSet
     * </pre>
     *
     * @param <T>  对象类型
     * @param type 被构造的类
     * @return 构造后的对象，构造失败返回{@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstanceIfPossible(Class<T> type) {
        Assert.notNull(type);

        // 原始类型
        if (type.isPrimitive()) {
            return (T) ClassUtils.getPrimitiveDefaultValue(type);
        }

        // 检查是否有默认实现
        if (DEFAULT_IMPLEMENTATIONS.containsKey(type)) {
            Supplier<T> supplier = (Supplier<T>) DEFAULT_IMPLEMENTATIONS.get(type);
            return supplier.get();
        }

        try {
            return newInstance(type);
        } catch (Exception e) {
            // ignore
            // 默认构造不存在的情况下查找其它构造
        }

        // 枚举
        if (type.isEnum()) {
            Object[] enumConstants = type.getEnumConstants();
            if (enumConstants != null && enumConstants.length > 0) {
                return (T) enumConstants[0];
            }
        }

        // 数组
        if (type.isArray()) {
            return (T) Array.newInstance(type.getComponentType(), 0);
        }

        final Constructor<T>[] constructors = getConstructors(type);
        Class<?>[] parameterTypes;
        for (Constructor<T> constructor : constructors) {
            parameterTypes = constructor.getParameterTypes();
            if (0 == parameterTypes.length) {
                continue;
            }
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            try {
                return constructor.newInstance(getDefaultValues(parameterTypes));
            } catch (Exception ignore) {
                // 构造出错时继续尝试下一种构造方式
            }
        }
        return null;
    }

    public static Type getTypeArgument(Type type) {
        return getTypeArgument(type, 0);
    }

    public static Type getTypeArgument(Type type, int index) {
        Type[] typeArguments = getTypeArguments(type);
        return null != typeArguments && typeArguments.length > index ? typeArguments[index] : null;
    }

    public static Type[] getTypeArguments(Type type) {
        if (null == type) {
            return null;
        } else {
            ParameterizedType parameterizedType = toParameterizedType(type);
            return null == parameterizedType ? null : parameterizedType.getActualTypeArguments();
        }
    }

    public static ParameterizedType toParameterizedType(Type type) {
        return toParameterizedType(type, 0);
    }

    public static ParameterizedType toParameterizedType(Type type, int interfaceIndex) {
        if (type instanceof ParameterizedType) {
            return (ParameterizedType) type;
        } else {
            if (type instanceof Class) {
                ParameterizedType[] generics = getGenerics((Class) type);
                if (generics.length > interfaceIndex) {
                    return generics[interfaceIndex];
                }
            }

            return null;
        }
    }

    public static ParameterizedType[] getGenerics(Class<?> clazz) {
        List<ParameterizedType> result = new ArrayList();
        Type genericSuper = clazz.getGenericSuperclass();
        if (null != genericSuper && !Object.class.equals(genericSuper)) {
            ParameterizedType parameterizedType = toParameterizedType(genericSuper);
            if (null != parameterizedType) {
                result.add(parameterizedType);
            }
        }

        Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (ArrayUtils.isNotEmpty(genericInterfaces)) {
            for (Type genericInterface : genericInterfaces) {
                ParameterizedType parameterizedType = toParameterizedType(genericInterface);
                if (null != parameterizedType) {
                    result.add(parameterizedType);
                }
            }
        }

        return (ParameterizedType[]) result.toArray(new ParameterizedType[0]);
    }
}
