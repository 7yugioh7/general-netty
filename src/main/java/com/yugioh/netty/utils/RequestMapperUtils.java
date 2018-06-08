package com.yugioh.netty.utils;

import com.yugioh.netty.http.server.annotation.RequestMapping;
import com.yugioh.netty.http.server.annotation.RequestMethod;
import com.yugioh.netty.http.server.business.BaseHandle;
import io.netty.handler.codec.http.HttpMethod;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Create By lieber
 * @Description 请求映射工具
 * @Date Create in 2018/6/7 16:49
 * @Modify By
 */
public class RequestMapperUtils {
    /**
     * 映射工具实例
     */
    private volatile static RequestMapperUtils instance;

    /**
     * 请求映射对应map
     */
    private static ConcurrentHashMap<String, BaseHandle> map;

    /**
     * 获取实例
     *
     * @return 工具实例
     */
    public static RequestMapperUtils getInstance() {
        if (instance == null) {
            synchronized (RequestMapperUtils.class) {
                if (instance == null) {
                    instance = init();
                }
            }
        }
        return instance;
    }

    /**
     * 请求工具初始化
     *
     * @return 实例对象
     */
    private static RequestMapperUtils init() {
        map = new ConcurrentHashMap<>(16);
        return new RequestMapperUtils();
    }

    /**
     * 获取映射关系
     */
    public void getMapper() {
        String packageName = PropertyUtils.getValue("/config/main.properties", "business.code.package");
        List<Class<BaseHandle>> list = ClassUtils.getAllAdaptor(packageName);
        if (list != null && list.size() > 0) {
            for (Class<BaseHandle> baseHandleClass : list) {
                if (baseHandleClass.isAnnotationPresent(RequestMapping.class)) {
                    try {
                        RequestMapping requestMapping = baseHandleClass.getAnnotation(RequestMapping.class);
                        String[] values = requestMapping.value();
                        if (values.length > 0) {
                            BaseHandle handle = baseHandleClass.newInstance();
                            for (String val : values) {
                                map.put(val.replaceAll("/[/]*/", "/"), handle);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 获取执行的处理器
     *
     * @param url 请求地址
     * @return 处理器
     */
    public BaseHandle getHandle(String url, HttpMethod method) {
        if (url == null || map == null || map.size() == 0) {
            return null;
        }
        BaseHandle baseHandle = map.get(url);
        if (baseHandle == null) {
            return null;
        }
        // 加入请求方式判断 GET/POST
        RequestMapping requestMapping = baseHandle.getClass().getAnnotation(RequestMapping.class);
        RequestMethod[] methods = requestMapping.method();
        if (methods.length > 0) {
            for (RequestMethod requestMethod : methods) {
                if (requestMethod.name().equals(method.name())) {
                    return baseHandle;
                }
            }
            return null;
        }
        return baseHandle;
    }

}
