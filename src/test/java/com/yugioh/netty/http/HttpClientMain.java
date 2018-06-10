package com.yugioh.netty.http;

import com.alibaba.fastjson.JSONObject;
import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.utils.Constants;
import com.yugioh.netty.utils.HttpClientUtils;
import com.yugioh.netty.utils.Md5Utils;
import com.yugioh.netty.utils.PropertyUtils;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

/**
 * @Author Create By lieber
 * @Description http请求客户端测试类
 * @Date Create in 2018/6/10 13:57
 * @Modify By
 */
public class HttpClientMain {


    @Test
    public void test() {
        String url = "http://127.0.0.1:7777/demo/test";
        JSONObject data = new JSONObject();
        data.put("orderId", System.currentTimeMillis());
        String result = HttpClientUtils.doPostString(url, this.getPostString(data));
        System.out.println("请求结果为：" + result);
    }


    private String getPostString(Object data) {
        String appId = PropertyUtils.getValue("/config/test.properties", "appId");
        String token = PropertyUtils.getValue("/config/test.properties", "token");
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setAppId(appId);
        commonRequest.setData(data);
        commonRequest.setTimestamp(System.currentTimeMillis());
        commonRequest.setSign(this.sign(commonRequest, token));
        return JSONObject.toJSONString(commonRequest);
    }

    private String sign(CommonRequest commonRequest, String token) {
        Map<String, Object> json = (Map<String, Object>) JSONObject.parse(JSONObject.toJSONString(commonRequest));
        TreeMap<String, Object> map = new TreeMap<>();
        map.putAll(json);
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            Object valueObj = map.get(key);
            if (valueObj != null) {
                String value = null;
                if (!(valueObj instanceof String)) {
                    value = JSONObject.toJSONString(valueObj);
                } else {
                    value = (String) valueObj;
                }
                if (!"".equals(value.trim()) && !"sign".equals(key) && !"appInfo".equals(key)) {
                    sb.append(key).append("=").append(value).append("&");
                }
            }
        }
        sb.append("key").append("=").append(token);
        return Md5Utils.md5encode(sb.toString(), Constants.ENCODING).toUpperCase();
    }

}
