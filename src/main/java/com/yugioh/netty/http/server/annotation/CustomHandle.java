package com.yugioh.netty.http.server.annotation;

import java.lang.annotation.*;

/**
 * @Author Create By lieber
 * @Description
 * @Date Create in 2018/6/8 12:15
 * @Modify By
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomHandle {
}
