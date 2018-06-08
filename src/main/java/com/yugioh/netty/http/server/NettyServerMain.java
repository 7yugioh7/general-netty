package com.yugioh.netty.http.server;

import com.yugioh.netty.utils.PropertyUtils;

/**
 * @Author Create By lieber
 * @Description netty服务入口方法
 * @Date Create in 2018/6/7 10:11
 * @Modify By
 */
public class NettyServerMain {

    public static void main(String[] args) {
        try {
            String portStr = PropertyUtils.getValue("/config/main.properties", "main.port");
            int port = 7777;
            if (portStr != null) {
                try {
                    port = Integer.valueOf(portStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            new NettyServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务启动失败");
        }
    }

}
