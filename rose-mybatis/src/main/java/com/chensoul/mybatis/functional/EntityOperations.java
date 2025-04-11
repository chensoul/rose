package com.chensoul.mybatis.functional;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@SuppressWarnings("unchecked")
public abstract class EntityOperations {

    public static <T> EntityUpdater<T> doUpdate(BaseMapper<T> baseMapper) {
        return new EntityUpdater<>(baseMapper);
    }

    public static <T> EntityCreator<T> doCreate(BaseMapper<T> baseMapper) {
        return new EntityCreator(baseMapper);
    }
}
