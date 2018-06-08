package com.yugioh.netty.http.server.business;

import com.yugioh.netty.http.server.annotation.CustomHandle;
import com.yugioh.netty.http.server.annotation.RequestMapping;
import com.yugioh.netty.utils.HttpResponseUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author Create By lieber
 * @Description 自由测试handle处理器
 * @Date Create in 2018/6/8 14:17
 * @Modify By
 */
@CustomHandle
@RequestMapping("/demo/test/free")
public class DemoFreeHandle extends BaseHandle {

    @Override
    public void freeHandle(ChannelHandlerContext ctx, HttpRequest msg) {
        HttpResponseUtils.response(ctx, msg, "成功访问了自由测试handle处理器");
    }
}
