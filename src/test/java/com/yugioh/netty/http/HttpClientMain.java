package com.yugioh.netty.http;

import com.alibaba.fastjson.JSONObject;
import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.domain.CommonResponse;
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
        String appId = PropertyUtils.getValue("/config/test.properties", "appId");
        String token = PropertyUtils.getValue("/config/test.properties", "token");
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setAppId(appId);
        commonRequest.setBody(JSONObject.toJSONString(data));
        commonRequest.setTimestamp(System.currentTimeMillis());
        commonRequest.setSign(commonRequest.sign(token));
        for (int i = 0; i < 20; i++) {
            HttpClientUtils.doPostString(url, JSONObject.toJSONString(commonRequest));
        }
    }

    @Test
    public void testAes() {
        String appId = PropertyUtils.getValue("/config/test.properties", "appId");
        String token = PropertyUtils.getValue("/config/test.properties", "token");
        String aesKey = PropertyUtils.getValue("/config/test.properties", "aesKey");
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setAppId(appId);
        commonRequest.setTimestamp(System.currentTimeMillis());
        commonRequest.setEncrypt(EncryptType.ASE.name());


        String url = "http://127.0.0.1:7777/demo/test";
        JSONObject data = new JSONObject();
        data.put("orderId", System.currentTimeMillis());
        commonRequest.setBody(AesUtils.getInstance().encrypt(data.toJSONString(), aesKey));
        commonRequest.setSign(commonRequest.sign(token));
        String result = HttpClientUtils.doPostString(url, JSONObject.toJSONString(commonRequest));
        CommonResponse response = JSONObject.parseObject(result, CommonResponse.class);
        System.out.println("解密后业务数据为：" + AesUtils.getInstance().decrypt(response.getBody(), aesKey));
    }

    @Test
    public void testDes() {
        String appId = PropertyUtils.getValue("/config/test.properties", "appId");
        String token = PropertyUtils.getValue("/config/test.properties", "token");
        String desKey = PropertyUtils.getValue("/config/test.properties", "desKey");
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setAppId(appId);
        commonRequest.setTimestamp(System.currentTimeMillis());
        commonRequest.setEncrypt(EncryptType.DES.name());


        String url = "http://127.0.0.1:7777/demo/test";
        JSONObject data = new JSONObject();
        data.put("orderId", System.currentTimeMillis());
        commonRequest.setBody(DesUtils.getInstance().encrypt(data.toJSONString(), desKey));
        commonRequest.setSign(commonRequest.sign(token));
        String result = HttpClientUtils.doPostString(url, JSONObject.toJSONString(commonRequest));
        CommonResponse response = JSONObject.parseObject(result, CommonResponse.class);
        System.out.println("解密后业务数据为：" + DesUtils.getInstance().decrypt(response.getBody(), desKey));
    }

    @Test
    public void testRsa() {
        String appId = PropertyUtils.getValue("/config/test.properties", "appId");
        String token = PropertyUtils.getValue("/config/test.properties", "token");
        String rsaPublicKey = PropertyUtils.getValue("/config/test.properties", "rsaPublicKey");
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setAppId(appId);
        commonRequest.setTimestamp(System.currentTimeMillis());
        commonRequest.setEncrypt(EncryptType.RSA.name());


        String url = "http://127.0.0.1:7777/demo/test";
        JSONObject data = new JSONObject();
        data.put("orderId", System.currentTimeMillis());
        commonRequest.setBody(RsaUtils.getInstance().encryptByPublicKey(data.toJSONString(), rsaPublicKey));
        commonRequest.setSign(commonRequest.sign(token));
        String result = HttpClientUtils.doPostString(url, JSONObject.toJSONString(commonRequest));
        CommonResponse response = JSONObject.parseObject(result, CommonResponse.class);
        System.out.println("解密后业务数据为：" + RsaUtils.getInstance().decryptByPublicKey(response.getBody(), rsaPublicKey));
    }

    @Test
    public void testRsaAes() {
        String appId = PropertyUtils.getValue("/config/test.properties", "appId");
        String token = PropertyUtils.getValue("/config/test.properties", "token");
        String rsaPublicKey = PropertyUtils.getValue("/config/test.properties", "rsaPublicKey");
        String aesKey = AesUtils.getInstance().getAesKey();
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setAppId(appId);
        commonRequest.setTimestamp(System.currentTimeMillis());
        commonRequest.setEncrypt(EncryptType.RSA_AES.name());
        commonRequest.setEncryptKey(RsaUtils.getInstance().encryptByPublicKey(aesKey, rsaPublicKey));

        String url = "http://127.0.0.1:7777/demo/test";
        JSONObject data = new JSONObject();
        data.put("orderId", System.currentTimeMillis());
        commonRequest.setBody(AesUtils.getInstance().encrypt(data.toJSONString(), aesKey));
        commonRequest.setSign(commonRequest.sign(token));
        String result = HttpClientUtils.doPostString(url, JSONObject.toJSONString(commonRequest));
        CommonResponse response = JSONObject.parseObject(result, CommonResponse.class);
        System.out.println("解密后业务数据为：" + AesUtils.getInstance().decrypt(response.getBody(), aesKey));
    }

    @Test
    public void testRsaDes() {
        String appId = PropertyUtils.getValue("/config/test.properties", "appId");
        String token = PropertyUtils.getValue("/config/test.properties", "token");
        String rsaPublicKey = PropertyUtils.getValue("/config/test.properties", "rsaPublicKey");
        String desKey = DesUtils.getInstance().getDesKey();

        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setAppId(appId);
        commonRequest.setTimestamp(System.currentTimeMillis());
        commonRequest.setEncrypt(EncryptType.RSA_DES.name());
        commonRequest.setEncryptKey(RsaUtils.getInstance().encryptByPublicKey(desKey, rsaPublicKey));

        String url = "http://127.0.0.1:7777/demo/test";
        JSONObject data = new JSONObject();
        data.put("orderId", System.currentTimeMillis());
        commonRequest.setBody(DesUtils.getInstance().encrypt(data.toJSONString(), desKey));
        commonRequest.setSign(commonRequest.sign(token));
        String result = HttpClientUtils.doPostString(url, JSONObject.toJSONString(commonRequest));
        CommonResponse response = JSONObject.parseObject(result, CommonResponse.class);
        System.out.println("解密后业务数据为：" + DesUtils.getInstance().decrypt(response.getBody(), desKey));
    }

}
