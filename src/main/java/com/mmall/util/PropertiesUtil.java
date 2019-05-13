package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author colaz
 * @date 2019/5/3
 **/
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties prop;

    static {
        String fileName = "mmall.properties";
        prop = new Properties();
        try {
            prop.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "utf-8"));
        } catch (IOException e) {
            logger.error("读取配置文件异常", e);
        }
    }

    public static String getProperty(String key) {
        String value = prop.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = prop.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }
}
