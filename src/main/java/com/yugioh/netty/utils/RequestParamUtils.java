package com.yugioh.netty.utils;

import com.alibaba.fastjson.JSONObject;
import com.yugioh.netty.http.server.entity.CommonRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Create By lieber
 * @Description 解析请求参数工具类
 * @Date Create in 2018/6/7 18:01
 * @Modify By
 */
public class RequestParamUtils {

    /**
     * 参数校验
     */
    private final static Pattern PATTERN = Pattern.compile("(^[']*|[']*$|(?<=['])[']+)");

    /**
     * 获取请求参数
     *
     * @param request 请求信息
     * @return 请求参数
     */
    public static CommonRequest parse(HttpRequest request) {
        if (request == null) {
            return null;
        }
        HttpMethod method = request.getMethod();
        String jsonStr = null;
        if (HttpMethod.GET == method) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
            Map<String, List<String>> parameters = decoder.parameters();
            Iterator<Map.Entry<String, List<String>>> iterator = parameters.entrySet().iterator();
            JSONObject jsonObject = new JSONObject();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> next = iterator.next();
                jsonObject.put(next.getKey(), replaceStr(next.getValue().get(0).trim()));
            }
            jsonStr = jsonObject.toString();
        } else if (HttpMethod.POST == method) {
            FullHttpRequest fullRequest = (FullHttpRequest) request;
            jsonStr = ConvertUtils.buf2Str(fullRequest.content());
        } else {
            System.out.println("不支持的请求方式");
        }
        if (jsonStr != null) {
            return JsonUtils.parseObject(jsonStr, CommonRequest.class);
        }
        return null;
    }

    /**
     * 替换不合法字符串
     *
     * @param str 待替换字符串
     * @return 替换后字符串
     */
    private static String replaceStr(String str) {
        Matcher matcher = PATTERN.matcher(str);
        return matcher.replaceAll("");
    }

}
