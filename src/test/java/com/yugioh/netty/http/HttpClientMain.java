package com.yugioh.netty.http;

import com.alibaba.fastjson.JSONObject;
import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.enums.EncryptType;
import com.yugioh.netty.utils.AesUtils;
import com.yugioh.netty.utils.DesUtils;
import com.yugioh.netty.utils.HttpClientUtils;
import com.yugioh.netty.utils.PropertyUtils;
import com.yugioh.netty.utils.rsa.RsaUtils;
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
        CommonRequest commonRequest = this.getPostString(data);
        String result = HttpClientUtils.doPostString(url, JSONObject.toJSONString(commonRequest));
        System.out.println("请求结果为：" + result);
    }

    /**
     * 生成请求字符串
     *
     * @param data 请求数据
     * @return 请求字符串
     */
    private CommonRequest getPostString(Object data) {
        String appId = PropertyUtils.getValue("/config/test.properties", "appId");
        String token = PropertyUtils.getValue("/config/test.properties", "token");
        String aesKey = PropertyUtils.getValue("/config/test.properties", "aesKey");
        String desKey = PropertyUtils.getValue("/config/test.properties", "desKey");
        String rsaPublicKey = PropertyUtils.getValue("/config/test.properties", "rsaPublicKey");
        String rsaPrivateKey = PropertyUtils.getValue("/config/test.properties", "rsaPrivateKey");
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setAppId(appId);
        String rsaAesKey = AesUtils.getInstance().getAesKey(256);
        String rsaDesKey = DesUtils.getInstance().getDesKey();
        commonRequest.setData(DesUtils.getInstance().encrypt(JSONObject.toJSONString(data), rsaDesKey));
        commonRequest.setTimestamp(System.currentTimeMillis());
         commonRequest.setEncryptKey(RsaUtils.getInstance().encryptByPublicKey(rsaDesKey, rsaPublicKey));
        commonRequest.setEncrypt(EncryptType.RSA_DES.name());
        commonRequest.setSign(commonRequest.sign(token));
        return commonRequest;
    }

}
