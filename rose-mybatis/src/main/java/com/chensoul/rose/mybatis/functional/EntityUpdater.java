package com.chensoul.rose.mybatis.functional;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chensoul.rose.core.exception.BusinessException;
import com.chensoul.rose.core.validation.Update;
import io.vavr.control.Try;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class EntityUpdater<T> extends BaseEntityOperation implements Loader<T>, UpdateHandler<T>, Executor<T> {

    private final BaseMapper<T> baseMapper;

    private T entity;

    private Consumer<T> successHook = t -> log.info("update success");

    private Consumer<? super Throwable> errorHook = e -> log.error("update error", e);

    public EntityUpdater(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public Optional<T> execute() {
        doValidate(this.entity, Update.class);
        T save = Try.of(() -> {
                    baseMapper.updateById(entity);
                    return this.entity;
                })
                .onSuccess(successHook)
                .onFailure(errorHook)
                .getOrNull();
        return Optional.ofNullable(save);
    }

    @Override
    public UpdateHandler<T> loadById(Serializable id) {
        Objects.requireNonNull(id, "id is null");
        T t = baseMapper.selectById(id);
        if (Objects.isNull(t)) {
            throw new BusinessException("entity not found");
        } else {
            this.entity = t;
        }
        return this;
    }

    @Override
    public UpdateHandler<T> load(Supplier<T> t) {
        this.entity = t.get();
        return this;
    }

    @Override
    public Executor<T> update(Consumer<T> consumer) {
        Objects.requireNonNull(entity, "entity is null");
        consumer.accept(this.entity);
        return this;
    }

    @Override
    public Executor<T> onSuccess(Consumer<T> consumer) {
        this.successHook = consumer;
        return this;
    }

    @Override
    public Executor<T> onError(Consumer<? super Throwable> consumer) {
        this.errorHook = consumer;
        return this;
    }
}
