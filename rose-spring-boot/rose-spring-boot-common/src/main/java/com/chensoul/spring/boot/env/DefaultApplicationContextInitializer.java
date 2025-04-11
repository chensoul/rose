package com.chensoul.spring.boot.env;

import com.chensoul.core.util.LauncherService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class DefaultApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        List<LauncherService> launcherList = new ArrayList<>();
        ServiceLoader.load(LauncherService.class).forEach(launcherList::add);

        launcherList.stream()
                .sorted(Comparator.comparing(LauncherService::getOrder))
                .collect(Collectors.toList())
                .forEach(launcherService -> launcherService.initialize(applicationContext));
    }
}
