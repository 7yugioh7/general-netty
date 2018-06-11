package com.yugioh.netty.http.server;


import com.yugioh.netty.http.server.business.Handle;
import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.domain.CommonResponse;
import com.yugioh.netty.http.server.domain.ParamCheckResult;
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
public class HttpNettyServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                String url = HttpServerUtils.getInstance().getUrl(request);
                if (Constants.ICON_URI.equals(url)) {
                    return;
                }
                Handle handle = RequestMapperUtils.getInstance().getHandle(url, request.getMethod());
                if (handle == null) {
                    HttpResponseUtils.response(ctx, request, new CommonResponse(404, "没有对应的路由", null));
                    return;
                }
                // 判断是否是自定义(自定义则不会进行权限、加密、参数校验等环节)
                if (RequestMapperUtils.getInstance().custom(handle)) {
                    handle.freeHandle(ctx, request);
                } else {
                    this.notCustomRequest(ctx, request, handle, url);
                }
            } catch (Exception e) {
                e.printStackTrace();
                HttpResponseUtils.response(ctx, request, new CommonResponse(500, "处理请求出错", null));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }

    /**
     * 非自定义请求处理
     *
     * @param ctx     请求通道
     * @param request 请求信息
     * @param handle  处理器信息
     */
    private void notCustomRequest(ChannelHandlerContext ctx, HttpRequest request, Handle handle, String url) {
        CommonRequest commonRequest = RequestParamUtils.parse(request);
        System.out.println("请求参数为 " + commonRequest);
        ParamCheckResult prevCheckParam = CommonRequestUtils.getInstance().prevCheckParam(commonRequest);
        if (!prevCheckParam.getSuccess()) {
            HttpResponseUtils.response(ctx, request, new CommonResponse(400, prevCheckParam.getMessage(), null));
            return;
        }
        // 经过了前置检验,就说明后面的响应必须要签名
        // 嵌入权限管理
        boolean permission = CommonRequestUtils.getInstance().permission(url, commonRequest.getAppInfo());
        CommonResponse response;
        if (!permission) {
            response = new CommonResponse(400, "您无此权限", null);
        } else {
            // 嵌入解密
            boolean needDecrypt = CommonRequestUtils.getInstance().needDecrypt(commonRequest);
            if (needDecrypt) {
                commonRequest = CommonRequestUtils.getInstance().decrypt(commonRequest);
            }
            // 嵌入参数校验
            ParamCheckResult paramCheckResult = handle.checkParam(commonRequest);
            paramCheckResult = CommonRequestUtils.getInstance().dealParamCheckResult(paramCheckResult);
            if (!paramCheckResult.getSuccess()) {
                response = new CommonResponse(400, paramCheckResult.getMessage(), null);
            } else {
                response = handle.handle(commonRequest);
            }
            if (needDecrypt) {
                response = CommonResponseUtils.getInstance().encrypt(response, commonRequest);
            }
        }
        // 响应之前,签名
        response = CommonResponseUtils.getInstance().sign(response, commonRequest.getAppInfo().getToken());
        HttpResponseUtils.response(ctx, request, response);
    }

}
