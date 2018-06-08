package com.yugioh.netty.http.server.business;

import com.yugioh.netty.http.server.annotation.RequestMethod;
import com.yugioh.netty.http.server.entity.CommonRequest;
import com.yugioh.netty.http.server.annotation.RequestMapping;
import com.yugioh.netty.http.server.entity.CommonResponse;

/**
 * @Author Create By lieber
 * @Description 测试handle处理器
 * @Date Create in 2018/6/7 16:31
 * @Modify By
 */
@RequestMapping(value = {"/demo/test"}, method = RequestMethod.GET)
public class DemoHandle implements BaseHandle {

    @Override
    public CommonResponse handle(CommonRequest request) {
        CommonResponse<String> commonResponse = new CommonResponse<>();
        commonResponse.setCode(200);
        commonResponse.setData("成功访问测试handle");
        return commonResponse;
    }
}
