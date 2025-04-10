package com.chensoul.mybatis.encrypt.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.chensoul.mybatis.encrypt.FieldSetProperty;
import com.chensoul.mybatis.encrypt.annotation.FieldBind;
import com.chensoul.mybatis.encrypt.annotation.FieldEncrypt;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class FieldSetPropertyHelper {

    private static boolean hasFieldEncrypt = false;

    private static boolean hasFieldBind = false;

    private static Map<Class<?>, List<FieldSetProperty>> clazzMap;

    private static Set<Class<?>> excludeClazzSet;

    private FieldSetPropertyHelper() {}

    public static void init(boolean var1, boolean var2) {
        hasFieldEncrypt = var1;
        hasFieldBind = var2;
        clazzMap = new ConcurrentHashMap<>();
        excludeClazzSet = new CopyOnWriteArraySet<>();
    }

    public static List<FieldSetProperty> getFieldSetPropertyList(Class<?> clazz) {
        if (excludeClazzSet.contains(clazz)) {
            return new ArrayList<>();
        }

        List<FieldSetProperty> fieldSetPropertyList = clazzMap.get(clazz);
        if (fieldSetPropertyList != null) {
            return fieldSetPropertyList;
        }

        if (clazz.isAssignableFrom(HashMap.class)) {
            excludeClazzSet.add(clazz);
        } else {
            List<FieldSetProperty> finalFieldSetPropertyList = new ArrayList<>();

            for (Field field : FieldUtils.getAllFields(clazz)) {
                FieldEncrypt fieldEncrypt = null;
                if (hasFieldEncrypt) {
                    fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
                    if (null != fieldEncrypt && !field.getType().isAssignableFrom(String.class)) {
                        throw new RuntimeException("annotation `@FieldEncrypt` only string types are supported.");
                    }
                }

                FieldBind fieldBind = null;
                if (hasFieldBind) {
                    fieldBind = field.getAnnotation(FieldBind.class);
                }
                if (fieldBind != null || fieldEncrypt != null) {
                    finalFieldSetPropertyList.add(new FieldSetProperty(field.getName(), fieldEncrypt, fieldBind));
                }
            }

            fieldSetPropertyList = finalFieldSetPropertyList;

            if (fieldSetPropertyList.isEmpty()) {
                excludeClazzSet.add(clazz);
            } else {
                clazzMap.put(clazz, fieldSetPropertyList);
            }
        }

        return fieldSetPropertyList;
    }

    public static boolean foreachValue(
            Configuration configuration, Object value, BiConsumer<MetaObject, FieldSetProperty> consumer) {
        if (value == null) {
            return Boolean.FALSE;
        }
        List<FieldSetProperty> fieldSetPropertyList = getFieldSetPropertyList(value.getClass());
        if (!CollectionUtils.isEmpty(fieldSetPropertyList)) {
            MetaObject metaObject = configuration.newMetaObject(value);
            fieldSetPropertyList.parallelStream()
                    .forEach(fieldSetProperty -> consumer.accept(metaObject, fieldSetProperty));
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
