package com.chensoul.upms.entity;

import com.chensoul.spring.boot.redis.cache.TransactionalCache;
import java.io.Serializable;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Getter
public abstract class AbstractCachedEntityService<K extends Serializable, V extends Serializable, E>
        extends AbstractEntityService {

    protected final TransactionalCache<K, V> cache;

    public AbstractCachedEntityService(ApplicationEventPublisher eventPublisher, TransactionalCache<K, V> cache) {
        super(eventPublisher);
        this.cache = cache;
    }

    public void publishEvictEvent(E event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            eventPublisher.publishEvent(event);
        } else {
            handleEvictEvent(event);
        }
    }

    public abstract void handleEvictEvent(E event);
}
