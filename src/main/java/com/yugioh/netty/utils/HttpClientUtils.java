package com.yugioh.netty.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @Author Create By lieber
 * @Description httpclient请求封装工具类
 * @Date Create in 2018/4/3 9:12
 * @Modify By
 */
public class HttpClientUtils {

    /**
     * 关闭使用的相关流
     *
     * @param response   响应流
     * @param httpclient httpclient相关流
     */
    private static void close(CloseableHttpResponse response, CloseableHttpClient httpclient) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (httpclient != null) {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送post请求
     *
     * @param url     请求地址
     * @param params  参数
     * @param charset 编码格式
     * @return 请求结果
     */
    public static String doPost(String url, Map<String, String> params, String charset) {
        RequestConfig config = getProxyConfig(url);
        if (charset == null) {
            charset = Constants.ENCODING;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String ret = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(config);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (params != null && !params.isEmpty()) {
                Set<Entry<String, String>> entrySet = params.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs.add(new BasicNameValuePair(key, value));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            ret = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(response, httpclient);
        }
        return ret;
    }

    /**
     * 发送post请求
     *
     * @param url    请求地址
     * @param string 请求参数
     * @return 请求结果
     */
    public static String doPostString(String url, String string) {
        RequestConfig config = getProxyConfig(url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String ret = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(config);
            System.out.println("请求内容为: " + string);
            HttpEntity entityReq = new StringEntity(string, CharsetUtils.get(Constants.ENCODING));
            httpPost.setEntity(entityReq);
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            ret = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(response, httpclient);
        }
        System.out.println("请求结果为: " + ret);
        return ret;
    }

    /**
     * https post请求
     *
     * @param url      请求地址
     * @param string   请求参数
     * @param path     证书地址
     * @param password 证书密码
     * @return 请求结果
     */
    public static String doSSLPostString(String url, String string, String path, String password) {
        RequestConfig config = getProxyConfig(url);
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        String ret = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream inStream = new FileInputStream(new File(path))) {
                keyStore.load(inStream, password.toCharArray());
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, password.toCharArray()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(config);
            HttpEntity entityReq = new StringEntity(string, CharsetUtils.get("utf-8"));
            httpPost.setEntity(entityReq);
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            ret = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            ret = new String(ret.getBytes("iso8859-1"), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(response, httpclient);
        }
        return ret;
    }

    /**
     * 获取代理配置
     *
     * @param url 请求地址
     * @return 请求配置
     */
    private static RequestConfig getProxyConfig(String url) {
        if (url == null) {
            return null;
        }
        RequestConfig config = null;
        // 从配置文件中读取是否需要使用代理
        String isUseProxy = PropertyUtils.getValue("/conf/proxy.properties", "isUseProxy");
        // 从配置文件中读取如果当前url在配置的urls中,是否需要使用代理
        String isUseProxyWhenContainUrl = PropertyUtils.getValue("/conf/proxy.properties", "containUrlIsUse");
        // 判断当前url是否需要使用代理
        boolean isUseProxyBool = isUseProxy != null && "true".equals(isUseProxy) &&
                isUseProxyWhenContainUrl != null && (("true".equals(isUseProxyWhenContainUrl) && isContainUrl(url))
                || ("false".equals(isUseProxyWhenContainUrl) && !isContainUrl(url)));
        if (isUseProxyBool) {
            // 如果需要使用代理
            String proxyIp = null;
            String port = null;
            // 判断当前使用http代理还是https代理
            if (url.toLowerCase().startsWith(Constants.HTTPS)) {
                // 如果以https开头
                proxyIp = PropertyUtils.getValue("/conf/proxy.properties", "httpsProxyIp");
                port = PropertyUtils.getValue("/conf/proxy.properties", "httpsProxyPort");
            } else if (url.toLowerCase().startsWith(Constants.HTTP)) {
                // 如果以http开头
                proxyIp = PropertyUtils.getValue("/conf/proxy.properties", "httpProxyIp");
                port = PropertyUtils.getValue("/conf/proxy.properties", "httpProxyPort");
            }
            try {
                Integer proxyPort = Integer.parseInt((port == null ? "" : port).replaceAll(" ", ""));
                if (proxyIp != null && !"".equals(proxyIp.trim()) && proxyPort > 0) {
                    HttpHost proxy = new HttpHost(proxyIp, proxyPort);
                    config = RequestConfig.custom().setProxy(proxy).build();
                } else {
                    config = RequestConfig.custom().build();
                }
            } catch (Exception e) {
                e.printStackTrace();
                config = RequestConfig.custom().build();
            }
        }
        return config;
    }

    /**
     * 判断当前url是否在配置文件中
     *
     * @param url 当前的url
     * @return true/false
     */
    private static boolean isContainUrl(String url) {
        if (url == null) {
            return false;
        }
        // 从配置文件中获取需要使用代理的ip或者域名
        String tempUrl = url;
        String urls = PropertyUtils.getValue("/conf/proxy.properties", "urls");
        if (urls == null) {
            return false;
        }
        urls = urls.replaceAll("，", ",");
        String[] urlArray = urls.split(",");
        tempUrl = tempUrl.replace("https://", "").replace("http://", "");
        tempUrl = tempUrl.substring(0, tempUrl.indexOf("/"));
        for (String str : urlArray) {
            if (tempUrl.equals(str)) {
                return true;
            }
        }
        return false;
    }

}
