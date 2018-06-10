package com.yugioh.netty.http.server.domain;

import com.yugioh.netty.http.server.entity.AppInfo;
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
     * 请求的appId对应的信息(此处可理解调用方的信息,在通过权限校验之后会将这个信息附带到请求参数中)
     */
    private AppInfo appInfo;
    /**
     * 请求接口签名
     */
    private String sign;
    /**
     * 请求数据
     */
    private Object data;
    /**
     * 请求时间
     */
    private Long timestamp;
    /**
     * 加密类型
     */
    private String encrypt;
    /**
     * 加密密钥
     */
    private String encryptKey;
}
