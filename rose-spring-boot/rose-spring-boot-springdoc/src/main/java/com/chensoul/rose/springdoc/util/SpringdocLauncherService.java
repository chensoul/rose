package com.chensoul.rose.springdoc.util;

import com.chensoul.rose.core.util.LauncherService;
import java.util.Properties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

public class SpringdocLauncherService implements LauncherService {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Properties props = System.getProperties();
        props.setProperty("spring.mvc.pathmatch.matching-handler", "ANT_PATH_MATCHER");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
