package com.yugioh.netty.http.server.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Create By lieber
 * @Description 公用请求信息
 * @Date Create in 2018/6/7 17:49
 * @Modify By
 */
@Data
public class CommonRequest implements Serializable {
    /**
     * 请求接口凭证
     */
    private String appId;
    /**
     * 请求接口签名
     */
    private String sign;
    /**
     * 请求数据
     */
    private String data;
    /**
     * 请求时间
     */
    private long timestamp;
    /**
     * 加密类型
     */
    private String encrypt;
    /**
     * 加密密钥
     */
    private String encryptKey;
}
