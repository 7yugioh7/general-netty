package com.yugioh.netty.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @Author Create By lieber
 * @Description 读取配置文件工具类
 * @Date Create in 2018/6/7 10:11
 * @Modify By
 */
public class PropertyUtils {

    /**
     * 获取配置文件
     *
     * @param path 配置文件路径
     * @return 配置文件对象
     */
    private static Properties getProperties(String path) {
        InputStream in = PropertyUtils.class.getResourceAsStream(path);
        if (in == null) {
            return null;
        }
        Properties property = new Properties();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, Constants.ENCODING));
            property.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }

    /**
     * 获取配置文件中的值
     *
     * @param path 配置文件路径
     * @param key  文件key
     * @return 文件key对应的值
     */
    public static String getValue(String path, String key) {
        Properties property = getProperties(path);
        if (property == null) {
            return null;
        }
        return (String) property.get(key);
    }

    /**
     * 设置值
     *
     * @param path  配置文件路径
     * @param key   文件对应key
     * @param value 文件对应值
     */
    public static void setValue(String path, String key, String value) {
        Properties property = getProperties(path);
        if (property == null) {
            return;
        }
        property.put(key, value);
    }

}
