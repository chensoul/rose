package com.chensoul.rose.env;

import com.chensoul.rose.core.util.LauncherService;
import com.chensoul.rose.core.util.PropsUtil;
import java.util.Properties;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动参数拓展
 *
 * @author smallchil
 */
public class CommonLauncherService implements LauncherService {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Properties props = System.getProperties();
        PropsUtil.setProperty(props, "spring.main.allow-bean-definition-overriding", "true");
    }
}
