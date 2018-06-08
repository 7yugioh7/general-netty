package com.yugioh.netty.http.server.business;

import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.domain.CommonResponse;
import com.yugioh.netty.http.server.domain.ParamCheckResult;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author Create By lieber
 * @Description 公用处理器
 * @Date Create in 2018/6/7 16:30
 * @Modify By
 */
public interface Handle {

    /**
     * 基础处理器
     *
     * @param commonRequest 请求信息
     * @return 响应信息
     */
    CommonResponse handle(CommonRequest commonRequest);

    /**
     * 校验参数
     *
     * @param commonRequest 请求参数信息(此处传入总请求参数,主要是为了允许某些必传参数可为空的情况,比如外部回调,没有appId等我们必传的参数)
     * @return 校验结果
     */
    ParamCheckResult checkParam(CommonRequest commonRequest);

    /**
     * 自由处理器
     *
     * @param ctx 通道信息
     * @param msg 请求信息
     */
    void freeHandle(ChannelHandlerContext ctx, HttpRequest msg);
}
