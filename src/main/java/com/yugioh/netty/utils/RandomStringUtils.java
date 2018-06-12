package com.yugioh.netty.utils;

import java.util.Random;

/**
 * @Author Create By lieber
 * @Description 随机字符串生成工具类
 * @Date Create in 2018/6/12 9:18
 * @Modify By
 */
public class RandomStringUtils {

    /**
     * 默认随机字符串长度
     */
    private final static int DEFAULT_RANDOM_LENGTH = 32;
    /**
     * 最大随机字符串长度
     */
    private final static int MAX_RANDOM_LENGTH = 64;
    /**
     * 随机字符串最短长度
     */
    private final static int MIN_RANDOM_LENGTH = 16;
    /**
     * 所有使用字符(乱序)
     */
    private final static String ALL_STR = "befOPQRSTcdzABrsWXYaZ012tuvwV34xyGHIghiEFU567jklmnopqJKLMNCD89";
    /**
     * 随机函数
     */
    private static Random random;
    /**
     * 实例
     */
    private volatile static RandomStringUtils instance;

    /**
     * 获取实例
     *
     * @return 实例对象
     */
    public static RandomStringUtils getInstance() {
        if (instance == null) {
            synchronized (RandomStringUtils.class) {
                if (instance == null) {
                    instance = new RandomStringUtils();
                    random = new Random();
                }
            }
        }
        return instance;
    }

    /**
     * 获取随机字符串
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public String getRandomString(int length) {
        if (length < MIN_RANDOM_LENGTH || length > MAX_RANDOM_LENGTH) {
            length = DEFAULT_RANDOM_LENGTH;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);
            sb.append(ALL_STR.charAt(number));
        }
        return sb.toString();
    }
}
