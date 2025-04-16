package com.chensoul.rose.upms.support.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEntityService {

    protected final ApplicationEventPublisher eventPublisher;
}
