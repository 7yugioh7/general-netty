package com.yugioh.netty.http.server.business;

import com.yugioh.netty.http.server.annotation.RequestMapping;
import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.domain.CommonResponse;
import com.yugioh.netty.http.server.domain.ParamCheckResult;
import com.yugioh.netty.http.server.domain.Response;
import com.yugioh.netty.http.server.entity.People;
import com.yugioh.netty.utils.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Create By lieber
 * @Description 测试handle处理器
 * @Date Create in 2018/6/7 16:31
 * @Modify By
 */
@RequestMapping(value = {"/demo/test"})
public class DemoHandle extends BaseHandle {

    @Override
    public Response handle(CommonRequest request) {
        System.out.println("handle中的请求参数为：" + request);
        Response<List<People>> response = new Response<>();
        List<People> peopleList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            People people = new People();
            people.setAge(i);
            people.setHeight(i * 1.1f);
            people.setWeight(i * 2.3f);
            people.setName(RandomStringUtils.getInstance().getRandomString(17));
            peopleList.add(people);
        }
        response.setCode(200);
        response.setMsg("成功");
        response.setBody(peopleList);
        return response;
    }

    @Override
    public ParamCheckResult checkParam(CommonRequest request) {
        return new ParamCheckResult(true, null);
    }
}
