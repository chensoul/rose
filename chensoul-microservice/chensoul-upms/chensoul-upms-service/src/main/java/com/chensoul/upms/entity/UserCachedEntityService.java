package com.chensoul.upms.entity;

import com.chensoul.spring.boot.redis.cache.TransactionalCache;
import com.chensoul.upms.event.UserCacheEvictEvent;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.event.TransactionalEventListener;

// @Service
public class UserCachedEntityService extends AbstractCachedEntityService<String, User, UserCacheEvictEvent> {

    public UserCachedEntityService(ApplicationEventPublisher eventPublisher, TransactionalCache<String, User> cache) {
        super(eventPublisher, cache);
    }

    @Override
    @TransactionalEventListener(classes = UserCacheEvictEvent.class)
    public void handleEvictEvent(UserCacheEvictEvent event) {
        List<String> keys = new ArrayList<>(2);
        keys.add(event.getNewPhone());
        if (StringUtils.isNotEmpty(event.getOldPhone()) && !event.getNewPhone().equals(event.getOldPhone())) {
            keys.add(event.getOldPhone());
        }
        cache.evict(keys);
    }
}
