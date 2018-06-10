package com.yugioh.netty.http.server.business;

import com.yugioh.netty.http.server.annotation.RequestMapping;
import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.domain.CommonResponse;
import com.yugioh.netty.http.server.domain.ParamCheckResult;

/**
 * @Author Create By lieber
 * @Description 测试handle处理器
 * @Date Create in 2018/6/7 16:31
 * @Modify By
 */
@RequestMapping(value = {"/demo/test"})
public class DemoHandle extends BaseHandle {

    @Override
    public CommonResponse handle(CommonRequest request) {
        CommonResponse<String> commonResponse = new CommonResponse<>();
        commonResponse.setCode(200);
        commonResponse.setData("成功访问测试handle");
        return commonResponse;
    }

    @Override
    public ParamCheckResult checkParam(CommonRequest request) {
//        return null;
         return new ParamCheckResult(true, null);
    }
}
