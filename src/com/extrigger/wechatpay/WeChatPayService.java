package com.extrigger.wechatpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 微信支付服务类
 * 实现统一下单、查询订单、关闭订单等核心支付功能
 */
public class WeChatPayService {
    
    private final WeChatPayConfig config;
    
    public WeChatPayService(WeChatPayConfig config) {
        this.config = config;
    }
    
    /**
     * 统一下单
     * 创建支付订单，返回支付参数
     */
    public WeChatPayResponse unifiedOrder(WeChatPayRequest request) throws Exception {
        // 设置配置参数
        request.setAppId(config.getAppId());
        request.setMchId(config.getMchId());
        request.setNotifyUrl(config.getNotifyUrl());
        
        // 生成随机字符串
        if (request.getNonceStr() == null) {
            request.setNonceStr(WeChatPayUtils.generateNonceStr());
        }
        
        // 生成时间戳
        if (request.getTimeStart() == null) {
            request.setTimeStart(WeChatPayUtils.generateTimeStamp());
        }
        
        // 生成签名
        Map<String, String> requestMap = request.toMap();
        String sign = WeChatPayUtils.generateSign(requestMap, config.getMchKey());
        request.setSign(sign);
        
        // 发送请求
        String url = config.getApiUrl() + "/pay/unifiedorder";
        String xmlData = WeChatPayUtils.mapToXml(request.toMap());
        String responseXml = sendPostRequest(url, xmlData);
        
        // 解析响应
        Map<String, String> responseMap = WeChatPayUtils.xmlToMap(responseXml);
        WeChatPayResponse response = new WeChatPayResponse(responseMap);
        
        // 如果成功，生成支付参数
        if (response.isSuccess()) {
            generatePayParams(response);
        }
        
        return response;
    }
    
    /**
     * 查询订单
     * 根据微信订单号或商户订单号查询订单状态
     */
    public WeChatPayResponse queryOrder(String transactionId, String outTradeNo) throws Exception {
        if (transactionId == null && outTradeNo == null) {
            throw new IllegalArgumentException("微信订单号和商户订单号不能同时为空");
        }
        
        // 构建查询参数
        Map<String, String> params = new java.util.HashMap<>();
        params.put("appid", config.getAppId());
        params.put("mch_id", config.getMchId());
        params.put("nonce_str", WeChatPayUtils.generateNonceStr());
        
        if (transactionId != null) {
            params.put("transaction_id", transactionId);
        }
        if (outTradeNo != null) {
            params.put("out_trade_no", outTradeNo);
        }
        
        // 生成签名
        String sign = WeChatPayUtils.generateSign(params, config.getMchKey());
        params.put("sign", sign);
        
        // 发送请求
        String url = config.getApiUrl() + "/pay/orderquery";
        String xmlData = WeChatPayUtils.mapToXml(params);
        String responseXml = sendPostRequest(url, xmlData);
        
        // 解析响应
        Map<String, String> responseMap = WeChatPayUtils.xmlToMap(responseXml);
        return new WeChatPayResponse(responseMap);
    }
    
    /**
     * 关闭订单
     * 关闭未支付的订单
     */
    public WeChatPayResponse closeOrder(String outTradeNo) throws Exception {
        if (outTradeNo == null || outTradeNo.trim().isEmpty()) {
            throw new IllegalArgumentException("商户订单号不能为空");
        }
        
        // 构建关闭参数
        Map<String, String> params = new java.util.HashMap<>();
        params.put("appid", config.getAppId());
        params.put("mch_id", config.getMchId());
        params.put("out_trade_no", outTradeNo);
        params.put("nonce_str", WeChatPayUtils.generateNonceStr());
        
        // 生成签名
        String sign = WeChatPayUtils.generateSign(params, config.getMchKey());
        params.put("sign", sign);
        
        // 发送请求
        String url = config.getApiUrl() + "/pay/closeorder";
        String xmlData = WeChatPayUtils.mapToXml(params);
        String responseXml = sendPostRequest(url, xmlData);
        
        // 解析响应
        Map<String, String> responseMap = WeChatPayUtils.xmlToMap(responseXml);
        return new WeChatPayResponse(responseMap);
    }
    
    /**
     * 生成支付参数
     * 根据不同的支付方式生成相应的支付参数
     */
    private void generatePayParams(WeChatPayResponse response) {
        if ("JSAPI".equals(response.getTradeType())) {
            // JSAPI支付参数
            response.setTimeStamp(WeChatPayUtils.generateTimeStamp());
            response.setNonceStrPay(WeChatPayUtils.generateNonceStr());
            response.setSignType("MD5");
            
            // 生成支付签名
            Map<String, String> payParams = new java.util.HashMap<>();
            payParams.put("appId", response.getAppId());
            payParams.put("timeStamp", response.getTimeStamp());
            payParams.put("nonceStr", response.getNonceStrPay());
            payParams.put("package", "prepay_id=" + response.getPrepayId());
            payParams.put("signType", response.getSignType());
            
            String paySign = WeChatPayUtils.generateSign(payParams, config.getMchKey());
            response.setPaySign(paySign);
            
        } else if ("NATIVE".equals(response.getTradeType())) {
            // 扫码支付，直接使用codeUrl
            // 无需额外处理
        } else if ("APP".equals(response.getTradeType())) {
            // APP支付参数
            response.setTimeStamp(WeChatPayUtils.generateTimeStamp());
            response.setNonceStrPay(WeChatPayUtils.generateNonceStr());
            response.setSignType("MD5");
            
            // 生成支付签名
            Map<String, String> payParams = new java.util.HashMap<>();
            payParams.put("appid", response.getAppId());
            payParams.put("partnerid", response.getMchId());
            payParams.put("prepayid", response.getPrepayId());
            payParams.put("package", "Sign=WXPay");
            payParams.put("noncestr", response.getNonceStrPay());
            payParams.put("timestamp", response.getTimeStamp());
            
            String paySign = WeChatPayUtils.generateSign(payParams, config.getMchKey());
            response.setPaySign(paySign);
        }
    }
    
    /**
     * 发送POST请求
     */
    private String sendPostRequest(String urlString, String xmlData) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(xmlData.getBytes(StandardCharsets.UTF_8).length));
            
            // 发送数据
            try (OutputStream os = connection.getOutputStream()) {
                os.write(xmlData.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            
            // 读取响应
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            
            return response.toString();
            
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * 验证支付通知签名
     */
    public boolean verifyPayNotify(Map<String, String> notifyData) {
        String sign = notifyData.get("sign");
        if (sign == null) {
            return false;
        }
        
        return WeChatPayUtils.verifySign(notifyData, config.getMchKey(), sign);
    }
    
    /**
     * 处理支付通知
     */
    public WeChatPayResponse handlePayNotify(String notifyXml) {
        Map<String, String> notifyData = WeChatPayUtils.xmlToMap(notifyXml);
        
        // 验证签名
        if (!verifyPayNotify(notifyData)) {
            throw new RuntimeException("支付通知签名验证失败");
        }
        
        return new WeChatPayResponse(notifyData);
    }
}