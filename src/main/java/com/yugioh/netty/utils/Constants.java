package com.yugioh.netty.utils;

/**
 * @Author Create By lieber
 * @Description
 * @Date Create in 2018/6/7 11:28
 * @Modify By
 */
public class Constants {

    /**
     * 是否SSL证书
     */
    public static boolean IS_SSL = false;

    /**
     * 默认访问的icon地址
     */
    public final static String ICON_URI = "/favicon.ico";


    /**
     * 编码格式
     */
    public final static String ENCODING = "UTF-8";

    /**
     * xml请求开头
     */
    public final static String STARTER_OF_XML = "<xml>";

    /**
     * 自定义必须实现方法名
     */
    public final static String FREE_MUST_METHOD = "freeHandle";

    /**
     * 非自定义必须实现方法名
     */
    public final static String NORMAL_MUST_METHOD = "handle";

    /**
     * 时间戳允许误差
     */
    public final static long TIMESTAMP_ERROR = 3 * 60 * 1000;
}
