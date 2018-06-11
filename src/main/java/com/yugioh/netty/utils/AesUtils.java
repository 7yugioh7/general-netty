package com.yugioh.netty.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Author Create By lieber
 * @Description AES加解密工具类
 * @Date Create in 2018/6/10 20:20
 * @Modify By
 */
public class AesUtils {

    /**
     * 128位密钥长度
     */
    private final static int LENGTH_OF_128 = 128;
    /**
     * 192位密钥长度
     */
    private final static int LENGTH_OF_192 = 192;
    /**
     * 256位密钥长度
     */
    private final static int LENGTH_OF_256 = 256;

    /**
     * 工具实例
     */
    private volatile static AesUtils instance;

    /**
     * 获取实例
     *
     * @return AES加解密实例
     */
    public static AesUtils getInstance() {
        if (instance == null) {
            synchronized (AesUtils.class) {
                if (instance == null) {
                    instance = new AesUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取Aes的密钥
     *
     * @param length 密钥长度
     * @return Aes密钥
     */
    public String getAesKey(int length) {
        String key = null;
        if (length == LENGTH_OF_128 || length == LENGTH_OF_192 || length == LENGTH_OF_256) {
            try {
                KeyGenerator kg = KeyGenerator.getInstance("AES");
                kg.init(length);
                SecretKey sk = kg.generateKey();
                byte[] b = sk.getEncoded();
                key = base64Encode(b);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return key;
    }

    /**
     * base64加密
     *
     * @param bytes 加密字符数组
     * @return 加密后字符串
     */
    private String base64Encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * base64解密
     *
     * @param base64Code 解密字符串
     * @return 解密后字符数组
     */
    private byte[] base64Decode(String base64Code) {
        try {
            return new BASE64Decoder().decodeBuffer(base64Code);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加密成为byte数组
     *
     * @param content    待加密内容
     * @param encryptKey 加密密钥
     * @return 加密后字符串
     */
    private byte[] encryptToBytes(String content, String encryptKey) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(encryptKey.getBytes());
            keyGenerator.init(128, secureRandom);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyGenerator.generateKey().getEncoded(), "AES"));
            return cipher.doFinal(content.getBytes(Constants.ENCODING));
        } catch (Exception e) {
            throw new RuntimeException("加密时初始化密钥出现异常");
        }
    }

    /**
     * @param content    加密内容
     * @param encryptKey 加密密钥
     * @return 加密后字符串
     */
    public String encrypt(String content, String encryptKey) {
        try {
            return base64Encode(encryptToBytes(content, encryptKey));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密字符串字符数组
     * @param decryptKey   加密密钥
     * @return 解密后字符串
     */
    private String decrypt(byte[] encryptBytes, String decryptKey) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(decryptKey.getBytes());
            keyGenerator.init(128, secureRandom);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyGenerator.generateKey().getEncoded(), "AES"));
            byte[] decryptBytes = cipher.doFinal(encryptBytes);
            return new String(decryptBytes, Constants.ENCODING);
        } catch (Exception e) {
            throw new RuntimeException("解密时初始化密钥出现异常");
        }
    }

    /**
     * AES解密
     *
     * @param encryptStr 待解密字符串
     * @param decryptKey 加密密钥
     * @return 解密后字符串
     */
    public String decrypt(String encryptStr, String decryptKey) {
        try {
            return decrypt(this.base64Decode(encryptStr), decryptKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
