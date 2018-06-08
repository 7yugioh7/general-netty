package com.yugioh.netty.http.server.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Create By lieber
 * @Description 公用响应
 * @Date Create in 2018/6/7 17:50
 * @Modify By
 */
@Data
public class CommonResponse<T> implements Serializable {
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
    private T data;

    public CommonResponse() {
    }

    public CommonResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
