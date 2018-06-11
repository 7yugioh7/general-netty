package com.yugioh.netty.utils;

import com.sun.crypto.provider.SunJCE;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.util.Random;

/**
 * @Author Create By lieber
 * @Description DES加解密工具类
 * @Date Create in 2018/6/11 13:59
 * @Modify By
 */
public class DesUtils {

    /**
     * 密钥长度
     */
    private final static int DES_KEY_LENGTH = 8;

    /**
     * 实例
     */
    private volatile static DesUtils instance;

    /**
     * 获取实例
     *
     * @return 实例对象
     */
    public static DesUtils getInstance() {
        if (instance == null) {
            synchronized (DesUtils.class) {
                if (instance == null) {
                    Security.addProvider(new SunJCE());
                    instance = new DesUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 加密字符串
     *
     * @param content    需加密的字符串
     * @param encryptKey 需加密的字符串
     * @return 加密后的字符串
     */
    public String encrypt(String content, String encryptKey) {
        try {
            return new BASE64Encoder().encode(encrypt(content.getBytes(), encryptKey));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 加密字节数组
     *
     * @param arrB       需加密的字节数组
     * @param encryptKey 加密密钥
     * @return 加密后的字节数组
     * @throws Exception 加密异常
     */
    private byte[] encrypt(byte[] arrB, String encryptKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("DES");
        Key key = getKey(encryptKey.getBytes());
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptCipher.doFinal(arrB);
    }


    /**
     * 解密字符串
     *
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     */
    public String decrypt(String strIn, String strKey) {
        try {
            return new String(decrypt(new BASE64Decoder().decodeBuffer(strIn), strKey));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception 解密异常
     */
    private byte[] decrypt(byte[] arrB, String strKey) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("DES");
        Key key = getKey(strKey.getBytes());
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        return decryptCipher.doFinal(arrB);
    }


    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位
     * 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     */
    private Key getKey(byte[] arrBTmp) {
        byte[] arrB = new byte[8];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return new SecretKeySpec(arrB, "DES");
    }

    /**
     * 获取随机密钥
     *
     * @return 密钥
     */
    public String getDesKey() {
        StringBuilder sb = new StringBuilder(DES_KEY_LENGTH);
        Random random = new Random();
        for (int i = 0; i < DES_KEY_LENGTH; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }
}
