package com.yugioh.netty.http.server.enums;

/**
 * @Author Create By lieber
 * @Description 支持的加密类型
 * @Date Create in 2018/6/8 15:19
 * @Modify By
 */
public enum EncryptType {

    /**
     * 非对称加密RSA
     */
    RSA,
    /**
     * 对称加密DES
     */
    DES,
    /**
     * 对称加密AES
     */
    ASE,
    /**
     * 混合加密RSA+AES
     */
    RSA_AES,
    /**
     * 混合加密RSA+DES
     */
    RSA_DES;

    EncryptType() {

    }

    /**
     * 通过名称获取枚举
     *
     * @param name 枚举名称
     * @return 枚举对象
     */
    public static EncryptType getForName(String name) {
        if (name == null) {
            return null;
        }
        EncryptType[] encryptTypes = EncryptType.values();
        for (EncryptType encryptType : encryptTypes) {
            if (encryptType.name().equals(name)) {
                return encryptType;
            }
        }
        return null;
    }
}
