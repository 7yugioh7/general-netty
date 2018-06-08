package com.yugioh.netty.utils;


import com.alibaba.fastjson.JSONObject;

/**
 * @Author Create By lieber
 * @Description json工具
 * @Date Create in 2018/6/7 14:18
 * @Modify By
 */
public class JsonUtils {

    /**
     * json字符串转json对象
     *
     * @param jsonString json字符串
     * @param clazz      对象类类型
     * @param <T>        对象
     * @return 转换后对象
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        T t = null;
        try {
            t = JSONObject.parseObject(jsonString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 对象转json字符串
     *
     * @param obj 待转换对象
     * @return 对象json字符串
     */
    public static String toJSON(Object obj) {
        return (JSONObject.toJSONString(obj));
    }
}
