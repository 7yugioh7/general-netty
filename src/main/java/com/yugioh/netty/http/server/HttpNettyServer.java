package com.yugioh.netty.http.server;

import com.yugioh.netty.utils.Constants;
import com.yugioh.netty.utils.PropertyUtils;
import com.yugioh.netty.utils.RequestMapperUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author Create By lieber
 * @Description netty服务
 * @Date Create in 2018/6/7 10:11
 * @Modify By
 */
public class HttpNettyServer {

    /**
     * 启动netty服务
     *
     * @param port 服务端口
     * @throws Exception 异常
     */
    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 服务启动之前,首先扫描所有的类
            RequestMapperUtils.getInstance().getMapper();
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new HttpServerChannelHandler());
            ChannelFuture f = b.bind(port).sync();
            try {
                Constants.IS_SSL = Boolean.valueOf(PropertyUtils.getValue("/config/main.properties", "main.isSSL"));
            } catch (Exception e) {
                Constants.IS_SSL = false;
            }
            System.out.println("NettyServer：" + port);
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("启动netty服务失败");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
