package com.yugioh.netty.utils.rsa;

import lombok.Data;

/**
 * @Author Create By lieber
 * @Description RSA加密密钥对
 * @Date Create in 2018/6/11 16:58
 * @Modify By
 */
@Data
public class KeyPairInfo {
    /**
     * 公钥
     */
    String privateKey;
    /**
     * 私钥
     */
    String publicKey;
    /**
     *
     */
    int keySize=0;

    public KeyPairInfo(int keySize) {
        this.keySize = keySize;
    }

    public KeyPairInfo(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
}
