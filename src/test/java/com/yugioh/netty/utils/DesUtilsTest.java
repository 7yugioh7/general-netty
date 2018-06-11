package com.yugioh.netty.utils;

import org.junit.Test;

/**
 * @Author Create By lieber
 * @Description DES加解密测试
 * @Date Create in 2018/6/11 14:48
 * @Modify By
 */
public class DesUtilsTest {

    @Test
    public void testEncrypt() {
        String key = "12345679";
        String text = "12345678132131232dsafjlksajdf;lkasjf;lksajfdoisjdlk;ajflksjalkfjsalkjflkjdsalkjflkjdsaflkdsjflkjaslkdjflksajdlkfjdsalkjflksajdlkjflkfjdsalkjflkjsalkjdsalkf1321321312321321312123213211111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111112345678132131232dsafjlksajdf;lkasjf;lksajfdoisjdlk;ajflksjalkfjsalkjflkjdsalkjflkjdsaflkdsjflkjaslkdjflksajdlkfjdsalkjflksajdlkjflkfjdsalkjflkjsalkjdsalkf13213213123213213121232132111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        System.out.println("明文字符串长度:"+text.length());
        long s = System.currentTimeMillis();
        String result1 = DesUtils.getInstance().encrypt(text,key);
        System.out.println("加密耗时："+(System.currentTimeMillis()-s));
        long s2 = System.currentTimeMillis();
        String result2 = DesUtils.getInstance().decrypt(result1, key);
        System.out.println("解密耗时："+(System.currentTimeMillis()-s2));
        System.out.println("密文："+result1);
        System.out.println("明文："+result2);
    }

}
