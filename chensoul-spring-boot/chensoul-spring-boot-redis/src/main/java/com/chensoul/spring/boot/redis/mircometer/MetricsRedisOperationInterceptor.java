package com.chensoul.spring.boot.redis.mircometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 操作 Metrics 拦截器
 */
public class MetricsRedisOperationInterceptor implements RedisOperationInterceptor, MeterBinder {

	private final ThreadLocal<Map<String, Long>> startTimeThreadLocal = ThreadLocal.withInitial(HashMap::new);

	private MeterRegistry registry;

	@Override
	public void before(Object wrapper, Object delegate, String methodName, Object[] args) {
		String meterName = createMeterName(methodName, args);
		Map<String, Long> startTimeMap = startTimeThreadLocal.get();
		startTimeMap.put(meterName, System.nanoTime());
	}

	@Override
	public void after(Object wrapper, Object delegate, String methodName, Object[] args, Object result,
			Throwable failure) {
		String meterName = createMeterName(methodName, args);
		recordTimer(meterName);
		count(meterName);
	}

	private void recordTimer(String meterName) {
		Map<String, Long> startTimeMap = startTimeThreadLocal.get();
		Timer timer = Timer.builder("Timer-" + meterName).register(registry);
		long amount = System.nanoTime() - startTimeMap.get(meterName);
		timer.record(amount, TimeUnit.NANOSECONDS);
	}

	private void count(String meterName) {
		Counter counter = Counter.builder("Counter-" + meterName).register(registry);
		counter.increment();
	}

	private String createMeterName(String methodName, Object... args) {
		return "mq-ops-" + methodName;
	}

	@Override
	public void bindTo(MeterRegistry registry) {
		this.registry = registry;
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}

}
