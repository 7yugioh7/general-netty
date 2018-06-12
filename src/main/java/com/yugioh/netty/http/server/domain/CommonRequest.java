package com.yugioh.netty.http.server.domain;

import com.alibaba.fastjson.JSONObject;
import com.yugioh.netty.http.server.entity.AppInfo;
import com.yugioh.netty.utils.Constants;
import com.yugioh.netty.utils.Md5Utils;
import lombok.Data;

import java.io.Serializable;
import java.util.TreeMap;

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
    private String body;
    /**
     * 请求时间
     */
    private Long timestamp;
    /**
     * 随机签名字符串
     */
    private String nonceStr;
    /**
     * 加密类型
     */
    private String encrypt;
    /**
     * 加密密钥
     */
    private String encryptKey;

    /**
     * 对请求签名
     *
     * @param token 签名令牌
     * @return 签名字符串
     */
    public String sign(String token) {
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(this));
        // 放入TreeMap对数据进行排序
        TreeMap<String, String> map = new TreeMap<>();
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.getString(key));
        }
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value != null && !"".equals(value.trim()) && !"sign".equals(key) && !"appInfo".equals(key)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        sb.append("key").append("=").append(token);
        return Md5Utils.md5encode(sb.toString(), Constants.ENCODING).toUpperCase();
    }
}
