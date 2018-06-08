package com.yugioh.netty.http.server.business;

import com.yugioh.netty.http.server.entity.CommonRequest;
import com.yugioh.netty.http.server.entity.CommonResponse;

/**
 * @Author Create By lieber
 * @Description 公用处理器
 * @Date Create in 2018/6/7 16:30
 * @Modify By
 */
public interface BaseHandle {

    /**
     * 基础处理器
     *
     * @param commonRequest 请求信息
     * @return 响应信息
     */
    CommonResponse handle(CommonRequest commonRequest);

}
