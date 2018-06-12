package com.yugioh.netty.http.server.domain;

import com.alibaba.fastjson.JSONObject;
import com.yugioh.netty.utils.Constants;
import com.yugioh.netty.utils.Md5Utils;
import com.yugioh.netty.utils.RandomStringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * @Author Create By lieber
 * @Description 公用响应
 * @Date Create in 2018/6/7 17:50
 * @Modify By
 */
@Data
public class CommonResponse implements Serializable {
    /**
     * 响应时间
     */
    private long time = System.currentTimeMillis();
    /**
     * 状态码
     */
    private int code;
    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private String body;

    /**
     * 随机签名字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    public CommonResponse() {
    }

    public CommonResponse(int code, String msg, String body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    /**
     * 响应签名
     *
     * @param token 签名令牌
     * @return 签名字符串
     */
    public String sign(String token) {
        if (this.nonceStr == null) {
            this.nonceStr = RandomStringUtils.getInstance().getRandomString(32);
        }
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(this));
        // 放入TreeMap对数据进行排序
        TreeMap<String, String> map = new TreeMap<>();
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.getString(key));
        }
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value != null && !"".equals(value.trim()) && !"sign".equals(key)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        sb.append("key").append("=").append(token);
        return Md5Utils.md5encode(sb.toString(), Constants.ENCODING).toUpperCase();
    }
}
