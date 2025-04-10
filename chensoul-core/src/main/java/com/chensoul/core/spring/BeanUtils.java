package com.chensoul.core.spring;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ObjectUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 获取所有的属性值为空属性名数组
     *
     * @param source 对象
     * @return 属性值
     */
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            if (beanWrapper.getPropertyValue(propertyName) == null) {
                nullPropertyNames.add(propertyName);
            }
        }
        return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);
    }

    /**
     * 单对象基于class创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, Class<V> desc) {
        if (ObjectUtils.isEmpty(source)) {
            return null;
        }
        if (ObjectUtils.isEmpty(desc)) {
            return null;
        }
        final V target = ReflectionUtils.newInstanceIfPossible(desc);
        return copy(source, target);
    }

    /**
     * 单对象基于对象创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, V desc) {
        if (ObjectUtils.isEmpty(source)) {
            return null;
        }
        if (ObjectUtils.isEmpty(desc)) {
            return null;
        }
        BeanCopier beanCopier = BeanCopierCache.INSTANCE.get(source.getClass(), desc.getClass(), null);
        beanCopier.copy(source, desc, null);
        return desc;
    }

    /**
     * 列表对象基于class创建拷贝
     *
     * @param sourceList 数据来源实体列表
     * @param desc       描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> List<V> copyList(List<T> sourceList, Class<V> desc) {
        if (ObjectUtils.isEmpty(sourceList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        return sourceList.stream()
                .map(source -> {
                    V target = ReflectionUtils.newInstanceIfPossible(desc);
                    copy(source, target);
                    return target;
                })
                .collect(Collectors.toList());
    }

    /**
     * bean拷贝到map
     *
     * @param bean 数据来源实体
     * @return map对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> copyToMap(T bean) {
        if (ObjectUtils.isEmpty(bean)) {
            return null;
        }
        return BeanMap.create(bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map       数据来源
     * @param beanClass bean类
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }
        if (ObjectUtils.isEmpty(beanClass)) {
            return null;
        }
        T bean = ReflectionUtils.newInstanceIfPossible(beanClass);
        return mapToBean(map, bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map  数据来源
     * @param bean bean对象
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }
        if (ObjectUtils.isEmpty(bean)) {
            return null;
        }
        BeanMap.create(bean).putAll(map);
        return bean;
    }

    /**
     * BeanCopier属性缓存<br>
     * 缓存用于防止多次反射造成的性能问题
     */
    public enum BeanCopierCache {

        /**
         * BeanCopier属性缓存单例
         */
        INSTANCE;

        private final ConcurrentReferenceHashMap<String, BeanCopier> cache = new ConcurrentReferenceHashMap<>();

        /**
         * 获得类与转换器生成的key在{@link BeanCopier}的Map中对应的元素
         *
         * @param srcClass    源Bean的类
         * @param targetClass 目标Bean的类
         * @param converter   转换器
         * @return Map中对应的BeanCopier
         */
        public BeanCopier get(Class<?> srcClass, Class<?> targetClass, Converter converter) {
            final String key = genKey(srcClass, targetClass, converter);
            return cache.getOrDefault(key, BeanCopier.create(srcClass, targetClass, converter != null));
        }

        /**
         * 获得类与转换器生成的key
         *
         * @param srcClass    源Bean的类
         * @param targetClass 目标Bean的类
         * @param converter   转换器
         * @return 属性名和Map映射的key
         */
        private String genKey(Class<?> srcClass, Class<?> targetClass, Converter converter) {
            final StringBuilder key =
                    new StringBuilder().append(srcClass.getName()).append('#').append(targetClass.getName());
            if (null != converter) {
                key.append('#').append(converter.getClass().getName());
            }
            return key.toString();
        }
    }
}
