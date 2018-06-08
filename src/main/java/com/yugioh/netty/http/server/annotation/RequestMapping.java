package com.yugioh.netty.http.server.annotation;

import java.lang.annotation.*;

/**
 * @Author Create By lieber
 * @Description 请求映射, 同spring mvc的RequestMapping
 * @Date Create in 2018/6/7 16:34
 * @Modify By
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * 对应路由
     *
     * @return 路由数组
     */
    String[] value() default {};

    /**
     * 请求方式
     *
     * @return 请求方式
     */
    RequestMethod[] method() default {};

}
