package com.yugioh.netty.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;

/**
 * @Author Create By lieber
 * @Description 转换方法
 * @Date Create in 2018/6/7 14:12
 * @Modify By
 */
public class ConvertUtils {

    /**
     * ByteBuf 转换为String
     *
     * @param buf ByteBuf对象
     * @return 字符串
     */
    public static String buf2Str(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        try {
            return new String(bytes, Constants.ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 任意对象转ByteBuf
     *
     * @param object 待转换对象
     * @return ByteBuf对象
     */
    public static ByteBuf obj2Buf(Object object) {
        if (object == null) {
            return null;
        }
        return Unpooled.copiedBuffer(JsonUtils.toJSON(object), CharsetUtil.UTF_8);
    }

}
