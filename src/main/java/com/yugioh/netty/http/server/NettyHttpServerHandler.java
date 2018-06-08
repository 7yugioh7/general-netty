package com.yugioh.netty.http.server;


import com.yugioh.netty.http.server.business.BaseHandle;
import com.yugioh.netty.http.server.entity.CommonRequest;
import com.yugioh.netty.http.server.entity.CommonResponse;
import com.yugioh.netty.utils.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author Create By lieber
 * @Description netty请求处理类
 * @Date Create in 2018/6/7 10:50
 * @Modify By
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                String url = HttpServerUtils.getInstance().getUrl(request);
                if (Constants.ICON_URI.equals(url)) {
                    return;
                }
                BaseHandle handle = RequestMapperUtils.getInstance().getHandle(url, request.getMethod());
                if (handle == null) {
                    HttpResponseUtils.response(ctx, request, new CommonResponse<>(404, "没有对应的路由", null));
                    return;
                }
                CommonRequest commonRequest = RequestParamUtils.parse(request);
                System.out.println("请求参数为 " + commonRequest);
                // 可在此处嵌入权限管理,和参数的校验及加解密等操作
                CommonResponse commonResponse = handle.handle(commonRequest);
                HttpResponseUtils.response(ctx, request, commonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                HttpResponseUtils.response(ctx, request, new CommonResponse<>(500, "处理请求出错", null));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
