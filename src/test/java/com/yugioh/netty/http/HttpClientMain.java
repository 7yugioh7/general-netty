package com.yugioh.netty.http;

import com.alibaba.fastjson.JSONObject;
import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.utils.HttpClientUtils;
import com.yugioh.netty.utils.PropertyUtils;
import org.junit.Test;

/**
 * @Author Create By lieber
 * @Description http请求客户端测试类
 * @Date Create in 2018/6/10 13:57
 * @Modify By
 */
public class HttpClientMain {

    /**
     * 测试请求
     */
    @Test
    public void test() {
        String url = "http://127.0.0.1:7777/demo/test";
        JSONObject data = new JSONObject();
        data.put("orderId", System.currentTimeMillis());
        String result = HttpClientUtils.doPostString(url, this.getPostString(data));
        System.out.println("请求结果为：" + result);
    }

    /**
     * 生成请求字符串
     *
     * @param data 请求数据
     * @return 请求字符串
     */
    private String getPostString(Object data) {
        String appId = PropertyUtils.getValue("/config/test.properties", "appId");
        String token = PropertyUtils.getValue("/config/test.properties", "token");
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setAppId(appId);
        commonRequest.setData(JSONObject.toJSONString(data));
        commonRequest.setTimestamp(System.currentTimeMillis());
        commonRequest.setSign(commonRequest.sign(token));
        return JSONObject.toJSONString(commonRequest);
    }

}
