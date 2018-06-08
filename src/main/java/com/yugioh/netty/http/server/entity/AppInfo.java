package com.yugioh.netty.http.server.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Create By lieber
 * @Description 三方调用信息
 * @Date Create in 2018/6/8 15:11
 * @Modify By
 */
@Data
public class AppInfo implements Serializable {

    /**
     * 唯一主键id
     */
    private String id;

    /**
     * 三方名称
     */
    private String name;

    /**
     * 颁发的三方唯一请求接口id
     */
    private String appId;

    /**
     * 请求接口令牌
     */
    private String token;

    /**
     * RSA加密公钥
     */
    private String rsaPublicKey;

    /**
     * RSA加密私钥
     */
    private String rsaPrivateKey;

    /**
     * AES加密密钥
     */
    private String aesKey;

    /**
     * DES加密密钥
     */
    private String desKey;

    // 其他常用字段......
}
