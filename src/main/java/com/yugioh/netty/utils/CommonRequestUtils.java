package com.yugioh.netty.utils;

import com.alibaba.fastjson.JSONObject;
import com.yugioh.netty.http.server.domain.CommonRequest;
import com.yugioh.netty.http.server.domain.ParamCheckResult;
import com.yugioh.netty.http.server.entity.AppInfo;
import com.yugioh.netty.http.server.enums.EncryptType;

import java.util.Map;
import java.util.TreeMap;

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
        if (StringUtils.isNull(commonRequest.getSign())) {
            return new ParamCheckResult(false, "必传参数sign为空");
        }
        // 获取appInfo
        AppInfo appInfo = this.getByAppId(commonRequest.getAppId());
        if (appInfo == null) {
            return new ParamCheckResult(false, "无效的appId");
        }
        String sign = this.sign(commonRequest, appInfo.getToken());
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
        System.out.println(appId);
        AppInfo appInfo = new AppInfo();
        String path = "/conf/test.properties";
        appInfo.setAppId(PropertyUtils.getValue(path, "appId"));
        appInfo.setToken(PropertyUtils.getValue(path, "token"));
        return new AppInfo();
    }

    /**
     * 签名
     *
     * @param commonRequest 请求参数
     * @param token         令牌
     * @return 签名
     */
    private String sign(CommonRequest commonRequest, String token) {
        Map<String, String> json = (Map<String, String>) JSONObject.parse(JSONObject.toJSONString(commonRequest));
        TreeMap<String, String> map = new TreeMap<>();
        map.putAll(json);
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value != null && !"".equals(value.trim()) && !"sign".equals(key) && !"appInfo".equals(key)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        sb.append("key").append("=").append(token);
        return Md5Utils.md5encode(sb.toString(), Constants.ENCODING).toUpperCase();
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
            switch (encryptType) {
                case ASE: {
                    break;
                }
                case DES: {
                    break;
                }
                case RSA: {
                    break;
                }
                case RSA_AES: {
                    break;
                }
                case RSA_DES: {
                    break;
                }
                default:
                    return commonRequest;
            }
        }
        return commonRequest;
    }
}