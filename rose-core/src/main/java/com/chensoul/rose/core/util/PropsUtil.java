package com.chensoul.rose.core.util;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class PropsUtil {

    /**
     * 设置配置值，已存在则跳过
     *
     * @param props property
     * @param key   key
     * @param value value
     */
    public static void setProperty(Properties props, String key, String value) {
        if (StringUtils.isEmpty(props.getProperty(key))) {
            props.setProperty(key, value);
        }
    }
}
