package com.yugioh.netty.utils;

import com.yugioh.netty.http.server.annotation.CustomHandle;
import com.yugioh.netty.http.server.annotation.RequestMapping;
import com.yugioh.netty.http.server.annotation.RequestMethod;
import com.yugioh.netty.http.server.business.Handle;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
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
    private static ConcurrentHashMap<String, Handle> map;

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
        List<Class<Handle>> list = ClassUtils.getAllAdaptor(packageName);
        if (list != null && list.size() > 0) {
            for (Class<Handle> baseHandleClass : list) {
                if (baseHandleClass.isAnnotationPresent(RequestMapping.class)) {
                    try {
                        RequestMapping requestMapping = baseHandleClass.getAnnotation(RequestMapping.class);
                        boolean custom = baseHandleClass.isAnnotationPresent(CustomHandle.class);
                        boolean add = this.hasMethod(baseHandleClass, custom ? Constants.FREE_MUST_METHOD : Constants.NORMAL_MUST_METHOD);
                        String[] values = requestMapping.value();
                        if (add && values.length > 0) {
                            Handle handle = baseHandleClass.newInstance();
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
    public Handle getHandle(String url, HttpMethod method) {
        if (url == null || map == null || map.size() == 0) {
            return null;
        }
        Handle baseHandle = map.get(url);
        // 加入请求方式判断 GET/POST
        if (this.isMethod(baseHandle, method)) {
            return baseHandle;
        }
        return null;
    }

    /**
     * 判断是否是支持的请求方式
     *
     * @param baseHandle 处理器
     * @param method     请求方式
     * @return 是否支持
     */
    private boolean isMethod(Handle baseHandle, HttpMethod method) {
        if (baseHandle == null) {
            return false;
        }
        RequestMapping requestMapping = baseHandle.getClass().getAnnotation(RequestMapping.class);
        RequestMethod[] methods = requestMapping.method();
        if (methods.length > 0) {
            for (RequestMethod requestMethod : methods) {
                if (requestMethod.name().equals(method.name())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 判断是否是自定义请求
     *
     * @param baseHandle 请求处理器
     * @return true/false
     */
    public boolean custom(Handle baseHandle) {
        return baseHandle != null && baseHandle.getClass().isAnnotationPresent(CustomHandle.class);
    }

    /**
     * 类是否包含某个方法
     *
     * @param clazz      类
     * @param methodName 方法名
     * @return true/false
     */
    private boolean hasMethod(Class<Handle> clazz, String methodName) {
        if (clazz == null || methodName == null) {
            return false;
        }
        Method[] methods = clazz.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
