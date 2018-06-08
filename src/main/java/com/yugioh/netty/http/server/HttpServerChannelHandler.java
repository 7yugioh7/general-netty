package com.yugioh.netty.http.server;

import com.yugioh.netty.utils.Constants;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.example.securechat.SecureChatSslContextFactory;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @Author Create By lieber
 * @Description http服务通道处理器
 * @Date Create in 2018/6/7 10:13
 * @Modify By
 */
public class HttpServerChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 判断是否需要SSL证书
        if (Constants.IS_SSL) {
            SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
            engine.setUseClientMode(false);
            pipeline.addLast("ssl", new SslHandler(engine));
        }
        //  http服务器端对request解码
        pipeline.addLast("decoder", new HttpRequestDecoder());
        //  http服务器端对response编码
        pipeline.addLast("encoder", new HttpResponseEncoder());
        // 支持post请求使用(参见https://stackoverflow.com/questions/23989217/posting-data-to-netty-with-apache-httpclient)
        pipeline.addLast(new HttpObjectAggregator(1048576));
        // 压缩
        pipeline.addLast("compressor", new HttpContentCompressor());
        // 自定义http处理
        pipeline.addLast("handler", new NettyHttpServerHandler());
    }
}
