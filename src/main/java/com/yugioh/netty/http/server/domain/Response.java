package com.yugioh.netty.http.server.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Create By lieber
 * @Description 响应
 * @Date Create in 2018/6/12 14:24
 * @Modify By
 */
@Data
public class Response<T> implements Serializable {
    /**
     * 状态码
     */
    private int code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 响应内容
     */
    private T body;

}
