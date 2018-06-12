package com.yugioh.netty.utils;

import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.domain.ParamCheckResult;
import com.yugioh.netty.http.server.entity.AppInfo;
import com.yugioh.netty.http.server.enums.EncryptType;
import com.yugioh.netty.utils.rsa.RsaUtils;

/**
 * @Author Create By lieber
 * @Description 公用请求工具类
 * @Date Create in 2018/6/8 17:14
 * @Modify By
 */
public class CommonRequestUtils {

    /**
     * 工具实例
     */
    private volatile static CommonRequestUtils instance;

    /**
     * 获取实例
     *
     * @return 实例对象
     */
    public static CommonRequestUtils getInstance() {
        if (instance == null) {
            synchronized (CommonRequestUtils.class) {
                if (instance == null) {
                    instance = new CommonRequestUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 前置参数校验
     *
     * @param commonRequest 参数
     * @return 校验结果
     */
    public ParamCheckResult prevCheckParam(CommonRequest commonRequest) {
        // 1.校验必传参数
        if (commonRequest == null) {
            return new ParamCheckResult(false, "必传参数为空");
        }
        // 1.1、appId
        if (StringUtils.isNull(commonRequest.getAppId())) {
            return new ParamCheckResult(false, "必传参数appId为空");
        }
        // 1.2、时间戳(放置重放攻击)
        if (commonRequest.getTimestamp() == null || commonRequest.getTimestamp() < System.currentTimeMillis() - Constants.TIMESTAMP_ERROR || commonRequest.getTimestamp() > System.currentTimeMillis() + Constants.TIMESTAMP_ERROR) {
            return new ParamCheckResult(false, "必传参数timestamp错误");
        }
        // 1.3 检验签名(防止参数被篡改)
        if (StringUtils.isNull(commonRequest.getSign())) {
            return new ParamCheckResult(false, "必传参数sign为空");
        }
        // 1.4 限流
        // 获取appInfo
        AppInfo appInfo = this.getByAppId(commonRequest.getAppId());
        if (appInfo == null) {
            return new ParamCheckResult(false, "无效的appId");
        }
        String sign = commonRequest.sign(appInfo.getToken());
        if (!commonRequest.getSign().toUpperCase().equals(sign)) {
            return new ParamCheckResult(false, "签名错误");
        }
        commonRequest.setAppInfo(appInfo);
        return new ParamCheckResult(true, null);
    }

    /**
     * 通过appId获取三方信息
     *
     * @param appId app标识
     * @return 三方信息
     */
    private AppInfo getByAppId(String appId) {
        // fixme 此处从数据库、缓存或者远程调用获取
        AppInfo appInfo = new AppInfo();
        String path = "/config/test.properties";
        String appId2 = PropertyUtils.getValue(path, "appId");
        if (appId == null || !appId.equals(appId2)) {
            return null;
        }
        String token = PropertyUtils.getValue(path, "token");
        String aesKey = PropertyUtils.getValue(path, "aesKey");
        String desKey = PropertyUtils.getValue(path, "desKey");
        String rsaPublicKey = PropertyUtils.getValue(path, "rsaPublicKey");
        String rsaPrivateKey = PropertyUtils.getValue(path, "rsaPrivateKey");
        appInfo.setAppId(appId2);
        appInfo.setToken(token);
        appInfo.setAesKey(aesKey);
        appInfo.setDesKey(desKey);
        appInfo.setRsaPrivateKey(rsaPrivateKey);
        appInfo.setRsaPublicKey(rsaPublicKey);
        return appInfo;
    }

    /**
     * 判断是否具有权限
     *
     * @param url     本次访问的url
     * @param appInfo 三方信息
     * @return true/false
     */
    public boolean permission(String url, AppInfo appInfo) {
        if (url == null) {
            return false;
        }
        if (appInfo == null) {
            return false;
        }
        // 其他判断
        return true;
    }

    /**
     * 是否需要加解密
     *
     * @param commonRequest 请求参数
     * @return true/false
     */
    public boolean needDecrypt(CommonRequest commonRequest) {
        if (commonRequest == null) {
            return false;
        }
        EncryptType encryptType = EncryptType.getForName(commonRequest.getEncrypt());
        return encryptType != null && commonRequest.getAppInfo() != null;
    }

    /**
     * 对参数解密
     *
     * @param commonRequest 请求信息
     * @return 加密后数据
     */
    public CommonRequest decrypt(CommonRequest commonRequest) {
        EncryptType encryptType = EncryptType.getForName(commonRequest.getEncrypt());
        if (encryptType != null && commonRequest.getAppInfo() != null) {
            // 判断加密类型
            String result;
            switch (encryptType) {
                case ASE: {
                    result = AesUtils.getInstance().decrypt(commonRequest.getBody(), commonRequest.getAppInfo().getAesKey());
                    break;
                }
                case DES: {
                    result = DesUtils.getInstance().decrypt(commonRequest.getBody(), commonRequest.getAppInfo().getDesKey());
                    break;
                }
                case RSA: {
                    // RSA有两种方式,一是我们颁发公私钥对,那么此时最好是调用端公钥加密,服务端私钥解密;
                    // 二是像支付宝一样由客户端配置,那么最好是客户端私钥加密,服务端公钥解密
                    // 此处先采用第一种模式
                    result = RsaUtils.getInstance().decryptByPrivateKey(commonRequest.getBody(), commonRequest.getAppInfo().getRsaPrivateKey());
                    break;
                }
                case RSA_AES: {
                    String key = RsaUtils.getInstance().decryptByPrivateKey(commonRequest.getEncryptKey(), commonRequest.getAppInfo().getRsaPrivateKey());
                    result = AesUtils.getInstance().decrypt(commonRequest.getBody(), key);
                    break;
                }
                case RSA_DES: {
                    String key = RsaUtils.getInstance().decryptByPrivateKey(commonRequest.getEncryptKey(), commonRequest.getAppInfo().getRsaPrivateKey());
                    result = DesUtils.getInstance().decrypt(commonRequest.getBody(), key);
                    break;
                }
                default:
                    return commonRequest;
            }
            commonRequest.setBody(result);
        }
        return commonRequest;
    }

    /**
     * 处理代码校验参数返回,因为代码编写可能会返回信息为空等
     *
     * @param paramCheckResult 待校验信息
     * @return 参数校验结果
     */
    public ParamCheckResult dealParamCheckResult(ParamCheckResult paramCheckResult) {
        if (paramCheckResult == null) {
            return new ParamCheckResult(false, "请求参数校验失败");
        }
        String message = paramCheckResult.getMessage();
        if (message == null) {
            paramCheckResult.setMessage("请求参数校验失败");
        }
        return paramCheckResult;
    }
}
