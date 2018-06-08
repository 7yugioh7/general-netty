package com.yugioh.netty.utils;

import java.security.MessageDigest;

/**
 * @Author Create By lieber
 * @Description MD5加密工具
 * @Date Create in 2017/6/20. 15:23
 * @Modify By
 */
class Md5Utils {

    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 字节数组转为字符串
     *
     * @param bytes 字节数组
     * @return 对应字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder resultSb = new StringBuilder();
        for (byte b : bytes) {
            resultSb.append(byteToHexString(b));
        }
        return resultSb.toString();
    }

    /**
     * 字节转为16进制字符串
     *
     * @param b 待转换字节
     * @return 16进制字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * MD5加密
     *
     * @param origin      待加密字符串
     * @param charsetName 编码格式
     * @return 加密后字符串
     */
    static String md5encode(String origin, String charsetName) {
        String resultString = null;
        try {
            if (!StringUtils.isNull(origin)) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                if (StringUtils.isNull(charsetName)) {
                    resultString = byteArrayToHexString(md.digest(origin.getBytes()));
                } else {
                    resultString = byteArrayToHexString(md.digest(origin.getBytes(charsetName)));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return resultString;
    }
}
