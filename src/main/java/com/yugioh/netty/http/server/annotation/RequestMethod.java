package com.yugioh.netty.http.server.annotation;

/**
 * @Author Create By lieber
 * @Description http请求方式
 * @Date Create in 2018/6/7 16:36
 * @Modify By
 */
public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private RequestMethod() {
    }
}
