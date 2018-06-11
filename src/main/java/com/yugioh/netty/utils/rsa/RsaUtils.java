package com.yugioh.netty.utils.rsa;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author Create By lieber
 * @Description RSA公私钥工具类
 * @Date Create in 2018/6/11 15:16
 * @Modify By
 */
public class RsaUtils {

    /**
     * 密钥对长度
     */
    private final static int KEY_SIZE = 2048;
    /**
     * 加密分段大小
     */
    private final static int ENCRYPT_SEGMENT_SIZE = 245;
    /**
     * 解密分段大小
     */
    private final static int DECRYPT_SEGMENT_SIZE = 256;

    /**
     * 工具实例
     */
    private volatile static RsaUtils instance;

    /**
     * 获取工具实例
     *
     * @return 工具实例对象
     */
    public static RsaUtils getInstance() {
        if (instance == null) {
            synchronized (RsaUtils.class) {
                if (instance == null) {
                    instance = new RsaUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 生成公钥、私钥对
     *
     * @return RSA加密密钥对
     */
    public KeyPairInfo getKeyPair() {
        return getKeyPair(KEY_SIZE);
    }

    /**
     * 生成公钥、私钥对
     *
     * @param keySize 密钥长度
     * @return RSA加密密钥对
     */
    private KeyPairInfo getKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keySize);
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            KeyPairInfo pairInfo = new KeyPairInfo(keySize);
            //公钥
            byte[] publicKeyByte = rsaPublicKey.getEncoded();
            String publicKeyString = Base64.encodeBase64String(publicKeyByte);
            pairInfo.setPublicKey(publicKeyString);
            //私钥
            byte[] privateKeyByte = rsaPrivateKey.getEncoded();
            String privateKeyString = Base64.encodeBase64String(privateKeyByte);
            pairInfo.setPrivateKey(privateKeyString);
            return pairInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取公钥对象
     *
     * @param publicKeyBase64 公钥base64编码格式
     * @return 公钥对象
     * @throws Exception 未捕获异常
     */
    private PublicKey getPublicKey(String publicKeyBase64) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyBase64));
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    /**
     * 获取私钥对象
     *
     * @param privateKeyBase64 私钥base64编码格式
     * @return 私钥对象
     * @throws Exception 未捕获异常
     */
    private PrivateKey getPrivateKey(String privateKeyBase64) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyBase64));
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
     * 公钥加密
     *
     * @param content         待加密内容
     * @param publicKeyBase64 公钥 base64 编码
     * @return 经过 base64 编码后的字符串
     */
    public String encryptByPublicKey(String content, String publicKeyBase64) {
        try {
            PublicKey publicKey = getPublicKey(publicKeyBase64);
            return encipher(content, publicKey, ENCRYPT_SEGMENT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥加密
     *
     * @param content         待加密内容
     * @param privateKeyBase64 私钥 base64 编码
     * @return 经过 base64 编码后的字符串
     */
    public String encryptByPrivateKey(String content, String privateKeyBase64) {
        try {
            PrivateKey privateKey = getPrivateKey(privateKeyBase64);
            return encipher(content, privateKey, ENCRYPT_SEGMENT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 分段加密
     *
     * @param cipherText  密文
     * @param key         加密秘钥
     * @param segmentSize 分段大小，<=0 不分段
     * @return 密文
     */
    private String encipher(String cipherText, Key key, int segmentSize) {
        try {
            // 用公钥加密
            byte[] srcBytes = cipherText.getBytes();
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] resultBytes;
            if (segmentSize > 0) {
                resultBytes = cipherDoFinal(cipher, srcBytes, segmentSize);
            } else {
                resultBytes = cipher.doFinal(srcBytes);
            }
            return Base64.encodeBase64String(resultBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分段大小加解密
     *
     * @param cipher      密码
     * @param srcBytes    源数据
     * @param segmentSize 分段大小
     * @return 加解密后字节数组
     */
    private byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize) throws Exception {
        if (segmentSize <= 0) {
            throw new RuntimeException("分段大小必须大于0");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }

    /**
     * 私钥解密
     *
     * @param contentBase64    待加密内容,base64 编码
     * @param privateKeyBase64 私钥 base64 编码
     * @return 明文
     */
    public String decryptByPrivateKey(String contentBase64, String privateKeyBase64) {
        try {
            PrivateKey privateKey = getPrivateKey(privateKeyBase64);
            return decipher(contentBase64, privateKey, DECRYPT_SEGMENT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥解密
     *
     * @param contentBase64   待解密内容,base64 编码
     * @param publicKeyBase64 公钥base64编码
     * @return 明文
     */
    public String decryptByPublicKey(String contentBase64, String publicKeyBase64) {
        try {
            PublicKey publicKey = getPublicKey(publicKeyBase64);
            return decipher(contentBase64, publicKey, DECRYPT_SEGMENT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 分段解密
     *
     * @param contentBase64 密文
     * @param key           解密秘钥
     * @param segmentSize   分段大小（小于等于0不分段）
     * @return 明文
     */
    private String decipher(String contentBase64, Key key, int segmentSize) {
        try {
            // 用私钥解密
            byte[] srcBytes = Base64.decodeBase64(contentBase64);
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher deCipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            deCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decBytes;
            if (segmentSize > 0) {
                decBytes = cipherDoFinal(deCipher, srcBytes, segmentSize);
            } else {
                decBytes = deCipher.doFinal(srcBytes);
            }
            return new String(decBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
