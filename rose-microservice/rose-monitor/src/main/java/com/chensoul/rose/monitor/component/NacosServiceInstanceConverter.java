package com.chensoul.rose.monitor.component;

import static java.util.Collections.emptyMap;

import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Configuration;

/**
 * 针对 nacos 2.x 服务注册处理
 */
@Configuration(proxyBeanMethods = false)
public class NacosServiceInstanceConverter extends DefaultServiceInstanceConverter {

    @Override
    protected Map<String, String> getMetadata(ServiceInstance instance) {
        return (instance.getMetadata() != null)
                ? instance.getMetadata().entrySet().stream()
                        .filter((e) -> e.getKey() != null && e.getValue() != null)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                : emptyMap();
    }
}
