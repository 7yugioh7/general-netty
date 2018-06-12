package com.yugioh.netty.utils;

import com.yugioh.netty.http.server.domain.CommonResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author Create By lieber
 * @Description 响应http请求工具类
 * @Date Create in 2018/6/7 14:10
 * @Modify By
 */
public class HttpResponseUtils {

    /**
     * 响应
     *
     * @param ctx 通道信息
     * @param req 请求信息
     * @param obj 响应对象
     */
    public static void response(ChannelHandlerContext ctx, HttpRequest req, Object obj) {
        if (obj == null) {
            obj = new CommonResponse(107, "接口正常处理完成,但是响应数据为空", null);
        }
        ByteBuf content = ConvertUtils.obj2Buf(obj);
        if (content == null) {
            content = ConvertUtils.obj2Buf("");
        }
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
        HttpHeaders headers = response.headers();
        headers.set(CONTENT_TYPE, "application/json; charset=UTF-8").
                set(ACCESS_CONTROL_ALLOW_ORIGIN, "*").
                set(ACCESS_CONTROL_ALLOW_METHODS, "POST")
                .set(ACCESS_CONTROL_ALLOW_HEADERS, "x-requested-with,content-type");
        HttpHeaders.setContentLength(response, content.readableBytes());
        execute(ctx, req, response);
    }

    /**
     * 执行响应--清空缓冲区
     *
     * @param ctx 通道信息
     * @param req 请求信息
     * @param res 响应信息
     */
    private static void execute(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse res) {
        if (HttpHeaders.isKeepAlive(req)) {
            res.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(res);
            ctx.flush();
        } else {
            ctx.write(res);
            ctx.flush();
            ctx.channel().writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
        }
    }

}
