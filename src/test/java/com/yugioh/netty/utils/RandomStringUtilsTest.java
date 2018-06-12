package com.yugioh.netty.utils;

import org.junit.Test;

/**
 * @Author Create By lieber
 * @Description 随机字符串工具测试类
 * @Date Create in 2018/6/12 9:47
 * @Modify By
 */
public class RandomStringUtilsTest {

    RandomStringUtils randomStringUtils = RandomStringUtils.getInstance();

    @Test
    public void test() {
        System.out.println(randomStringUtils.getRandomString(32));
    }

}
