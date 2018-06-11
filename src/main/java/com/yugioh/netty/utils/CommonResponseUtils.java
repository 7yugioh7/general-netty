package com.yugioh.netty.utils;

import com.alibaba.fastjson.JSONObject;
import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.domain.CommonResponse;
import com.yugioh.netty.http.server.enums.EncryptType;
import com.yugioh.netty.utils.rsa.RsaUtils;

/**
 * @Author Create By lieber
 * @Description
 * @Date Create in 2018/6/11 21:21
 * @Modify By
 */
public class CommonResponseUtils {

    /**
     * 实例
     */
    private volatile static CommonResponseUtils instance;

    /**
     * 获取实例
     *
     * @return 实例对象
     */
    public static CommonResponseUtils getInstance() {
        if (instance == null) {
            synchronized (CommonResponseUtils.class) {
                if (instance == null) {
                    instance = new CommonResponseUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 响应签名
     *
     * @param response 响应对象
     * @param token    签名令牌
     * @return 响应
     */
    public CommonResponse sign(CommonResponse response, String token) {
        if (response != null) {
            response.setSign(response.sign(token));
        }
        return response;
    }

    /**
     * 响应加密
     *
     * @param response 响应对象
     * @param commonRequest  请求信息
     * @return 响应
     */
    public CommonResponse encrypt(CommonResponse response, CommonRequest commonRequest) {
        EncryptType encryptType = EncryptType.getForName(commonRequest.getEncrypt());
        if (encryptType != null && commonRequest.getAppInfo() != null && response != null && response.getData() != null) {
            // 判断加密类型
            String result;
            switch (encryptType) {
                case ASE: {
                    result = AesUtils.getInstance().encrypt(JSONObject.toJSONString(response.getData()), commonRequest.getAppInfo().getAesKey());
                    break;
                }
                case DES: {
                    result = DesUtils.getInstance().encrypt(JSONObject.toJSONString(response.getData()), commonRequest.getAppInfo().getDesKey());
                    break;
                }
                case RSA: {
                    result = RsaUtils.getInstance().encryptByPrivateKey(JSONObject.toJSONString(response.getData()), commonRequest.getAppInfo().getRsaPrivateKey());
                    break;
                }
                case RSA_AES: {
                    String key = RsaUtils.getInstance().decryptByPrivateKey(commonRequest.getEncryptKey(), commonRequest.getAppInfo().getRsaPrivateKey());
                    result = AesUtils.getInstance().encrypt(JSONObject.toJSONString(response.getData()), key);
                    break;
                }
                case RSA_DES: {
                    String key = RsaUtils.getInstance().decryptByPrivateKey(commonRequest.getEncryptKey(), commonRequest.getAppInfo().getRsaPrivateKey());
                    result = DesUtils.getInstance().encrypt(JSONObject.toJSONString(response.getData()), key);
                    break;
                }
                default:
                    return response;
            }
            response.setData(result);
        }
        return response;
    }
}
