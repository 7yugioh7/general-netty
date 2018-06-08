package com.yugioh.netty.utils;

import io.netty.handler.codec.http.HttpRequest;

import java.net.URI;

/**
 * @Author Create By lieber
 * @Description http服务工具类
 * @Date Create in 2018/6/8 9:24
 * @Modify By
 */
public class HttpServerUtils {

    /**
     * 实例
     */
    private volatile static HttpServerUtils instance;

    /**
     * 获取实例
     *
     * @return 实例对象
     */
    public static HttpServerUtils getInstance() {
        if (instance == null) {
            synchronized (HttpServerUtils.class) {
                if (instance == null) {
                    instance = new HttpServerUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取请求路由
     *
     * @param request 请求信息
     * @return 请求路由
     */
    public String getUrl(HttpRequest request) {
        try {
            URI uri = new URI(request.getUri());
            String url = uri.getPath();
            url = url.replaceAll("/[/]*/", "/");
            System.out.println("本次请求的路由为：" + url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取请求路由失败");
            return "";
        }
    }

}
