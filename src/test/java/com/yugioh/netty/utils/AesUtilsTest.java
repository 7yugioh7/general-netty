package com.yugioh.netty.utils;

import org.junit.Test;

/**
 * @Author Create By lieber
 * @Description
 * @Date Create in 2018/6/11 18:27
 * @Modify By
 */
public class AesUtilsTest {

    private final static AesUtils aesUtils = AesUtils.getInstance();

    @Test
    public void testKey() {
        String result = aesUtils.getAesKey(256);
        System.out.println(result);
    }

}
