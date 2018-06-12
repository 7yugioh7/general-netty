package com.yugioh.netty.http.server.business;

import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.domain.ParamCheckResult;
import com.yugioh.netty.http.server.domain.Response;
import com.yugioh.netty.utils.HttpResponseUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author Create By lieber
 * @Description
 * @Date Create in 2018/6/8 14:12
 * @Modify By
 */
public class BaseHandle implements Handle {

    @Override
    public Response handle(CommonRequest commonRequest) {
        return null;
    }

    @Override
    public ParamCheckResult checkParam(CommonRequest commonRequest) {
        return null;
    }

    @Override
    public void freeHandle(ChannelHandlerContext ctx, HttpRequest msg) {
        HttpResponseUtils.response(ctx, msg, "访问的接口不存在");
    }
}
