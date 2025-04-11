package com.chensoul.spring.boot.micrometer;

import feign.Retryer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * 异步记录监控指标
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 4.0.4
 */
public abstract class Micrometers {

    public static final Retryer DEFAULT = new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), 3);

    private static final ExecutorService asyncExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            new BasicThreadFactory.Builder().namingPattern("metric-pool-%d").build());

    public static void async(Runnable runnable) {
        asyncExecutor.execute(runnable);
    }
}
