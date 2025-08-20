package com.example.wechatpay.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP客户端工具类
 * 用于发送微信支付API请求
 */
public class HttpClientUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    
    /**
     * 发送GET请求
     * 
     * @param url 请求URL
     * @param headers 请求头
     * @return 响应内容
     */
    public static String doGet(String url, Map<String, String> headers) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            
            // 设置请求头
            if (headers != null) {
                headers.forEach(httpGet::addHeader);
            }
            
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                }
            }
        }
        return null;
    }
    
    /**
     * 发送POST请求
     * 
     * @param url 请求URL
     * @param headers 请求头
     * @param body 请求体
     * @return 响应内容
     */
    public static String doPost(String url, Map<String, String> headers, String body) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            
            // 设置请求头
            if (headers != null) {
                headers.forEach(httpPost::addHeader);
            }
            
            // 设置请求体
            if (body != null) {
                StringEntity stringEntity = new StringEntity(body, StandardCharsets.UTF_8);
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
            }
            
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                }
            }
        }
        return null;
    }
    
    /**
     * 发送带签名的POST请求（用于微信支付API v3）
     * 
     * @param url 请求URL
     * @param body 请求体
     * @param mchId 商户号
     * @param serialNo 证书序列号
     * @param privateKey 私钥
     * @return 响应内容
     */
    public static String doPostWithSign(String url, String body, String mchId, 
                                       String serialNo, PrivateKey privateKey) throws Exception {
        // 生成签名
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = SignatureUtil.generateNonceStr();
        
        // 获取URL路径
        String urlPath = url.replace("https://api.mch.weixin.qq.com", "");
        
        // 构建签名消息
        String message = SignatureUtil.buildSignMessage("POST", urlPath, timestamp, nonceStr, body);
        
        // 生成签名
        String signature = SignatureUtil.sign(message, privateKey);
        
        // 生成Authorization头
        String authorization = SignatureUtil.getAuthorization(mchId, serialNo, nonceStr, timestamp, signature);
        
        // 设置请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authorization);
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        
        // 发送请求
        return doPost(url, headers, body);
    }
    
    /**
     * 发送带签名的GET请求（用于微信支付API v3）
     * 
     * @param url 请求URL
     * @param mchId 商户号
     * @param serialNo 证书序列号
     * @param privateKey 私钥
     * @return 响应内容
     */
    public static String doGetWithSign(String url, String mchId, 
                                      String serialNo, PrivateKey privateKey) throws Exception {
        // 生成签名
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = SignatureUtil.generateNonceStr();
        
        // 获取URL路径
        String urlPath = url.replace("https://api.mch.weixin.qq.com", "");
        
        // 构建签名消息
        String message = SignatureUtil.buildSignMessage("GET", urlPath, timestamp, nonceStr, "");
        
        // 生成签名
        String signature = SignatureUtil.sign(message, privateKey);
        
        // 生成Authorization头
        String authorization = SignatureUtil.getAuthorization(mchId, serialNo, nonceStr, timestamp, signature);
        
        // 设置请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authorization);
        headers.put("Accept", "application/json");
        
        // 发送请求
        return doGet(url, headers);
    }
    
    /**
     * 解析响应结果
     * 
     * @param response 响应内容
     * @param clazz 目标类型
     * @return 解析后的对象
     */
    public static <T> T parseResponse(String response, Class<T> clazz) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        
        try {
            return JSON.parseObject(response, clazz);
        } catch (Exception e) {
            logger.error("解析响应失败: {}", response, e);
            return null;
        }
    }
}