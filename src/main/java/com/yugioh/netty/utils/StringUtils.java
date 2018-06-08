package com.yugioh.netty.utils;

/**
 * @Author Create By lieber
 * @Description 字符串工具类
 * @Date Create in 2018/6/8 16:58
 * @Modify By
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isNull(String str) {
        return str == null || "".equals(str.trim());
    }

}
