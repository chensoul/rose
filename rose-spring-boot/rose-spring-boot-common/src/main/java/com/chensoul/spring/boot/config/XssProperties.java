package com.chensoul.spring.boot.config;

import com.chensoul.core.CommonConstants;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@ConfigurationProperties(CommonConstants.PROJECT_NAME + ".xss")
public class XssProperties {

    /**
     * 开启xss
     */
    private Boolean enabled = true;

    /**
     * 放行url
     */
    private List<String> excludeUrls = new ArrayList<>();
}
