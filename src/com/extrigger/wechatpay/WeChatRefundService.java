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
 * 微信退款服务类
 * 实现申请退款、查询退款等退款功能
 */
public class WeChatRefundService {
    
    private final WeChatPayConfig config;
    
    public WeChatRefundService(WeChatPayConfig config) {
        this.config = config;
    }
    
    /**
     * 申请退款
     * 对已支付的订单申请退款
     */
    public WeChatRefundResponse refund(WeChatRefundRequest request) throws Exception {
        // 设置配置参数
        request.setAppId(config.getAppId());
        request.setMchId(config.getMchId());
        request.setNotifyUrl(config.getRefundNotifyUrl());
        
        // 生成随机字符串
        if (request.getNonceStr() == null) {
            request.setNonceStr(WeChatPayUtils.generateNonceStr());
        }
        
        // 生成签名
        Map<String, String> requestMap = request.toMap();
        String sign = WeChatPayUtils.generateSign(requestMap, config.getMchKey());
        request.setSign(sign);
        
        // 发送请求
        String url = config.getApiUrl() + "/secapi/pay/refund";
        String xmlData = WeChatPayUtils.mapToXml(request.toMap());
        String responseXml = sendPostRequest(url, xmlData);
        
        // 解析响应
        Map<String, String> responseMap = WeChatPayUtils.xmlToMap(responseXml);
        return new WeChatRefundResponse(responseMap);
    }
    
    /**
     * 查询退款
     * 根据微信订单号、商户订单号、微信退款单号或商户退款单号查询退款状态
     */
    public WeChatRefundResponse queryRefund(String transactionId, String outTradeNo, 
                                          String refundId, String outRefundNo) throws Exception {
        if (transactionId == null && outTradeNo == null && 
            refundId == null && outRefundNo == null) {
            throw new IllegalArgumentException("查询参数不能全部为空");
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
        if (refundId != null) {
            params.put("refund_id", refundId);
        }
        if (outRefundNo != null) {
            params.put("out_refund_no", outRefundNo);
        }
        
        // 生成签名
        String sign = WeChatPayUtils.generateSign(params, config.getMchKey());
        params.put("sign", sign);
        
        // 发送请求
        String url = config.getApiUrl() + "/pay/refundquery";
        String xmlData = WeChatPayUtils.mapToXml(params);
        String responseXml = sendPostRequest(url, xmlData);
        
        // 解析响应
        Map<String, String> responseMap = WeChatPayUtils.xmlToMap(responseXml);
        return new WeChatRefundResponse(responseMap);
    }
    
    /**
     * 申请退款（简化版）
     * 提供常用的退款场景
     */
    public WeChatRefundResponse refund(String outTradeNo, String transactionId, 
                                     int totalFee, int refundFee, String refundDesc) throws Exception {
        WeChatRefundRequest request = new WeChatRefundRequest();
        request.setOutTradeNo(outTradeNo);
        request.setTransactionId(transactionId);
        request.setTotalFee(totalFee);
        request.setRefundFee(refundFee);
        request.setRefundDesc(refundDesc);
        request.setOutRefundNo(WeChatPayUtils.generateOutRefundNo());
        
        return refund(request);
    }
    
    /**
     * 全额退款
     */
    public WeChatRefundResponse fullRefund(String outTradeNo, String transactionId, 
                                         int totalFee, String refundDesc) throws Exception {
        return refund(outTradeNo, transactionId, totalFee, totalFee, refundDesc);
    }
    
    /**
     * 部分退款
     */
    public WeChatRefundResponse partialRefund(String outTradeNo, String transactionId, 
                                            int totalFee, int refundFee, String refundDesc) throws Exception {
        if (refundFee > totalFee) {
            throw new IllegalArgumentException("退款金额不能大于订单金额");
        }
        return refund(outTradeNo, transactionId, totalFee, refundFee, refundDesc);
    }
    
    /**
     * 发送POST请求（带证书）
     * 退款接口需要使用证书
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
            
            // TODO: 这里需要加载商户证书
            // 实际项目中需要使用SSL证书进行双向认证
            // 可以使用KeyStore或直接加载证书文件
            
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
     * 验证退款通知签名
     */
    public boolean verifyRefundNotify(Map<String, String> notifyData) {
        String sign = notifyData.get("sign");
        if (sign == null) {
            return false;
        }
        
        return WeChatPayUtils.verifySign(notifyData, config.getMchKey(), sign);
    }
    
    /**
     * 处理退款通知
     */
    public WeChatRefundResponse handleRefundNotify(String notifyXml) {
        Map<String, String> notifyData = WeChatPayUtils.xmlToMap(notifyXml);
        
        // 验证签名
        if (!verifyRefundNotify(notifyData)) {
            throw new RuntimeException("退款通知签名验证失败");
        }
        
        return new WeChatRefundResponse(notifyData);
    }
    
    /**
     * 检查退款状态
     */
    public boolean isRefundSuccess(WeChatRefundResponse response) {
        return response.isSuccess() && response.getRefundId() != null;
    }
    
    /**
     * 获取退款状态描述
     */
    public String getRefundStatusDesc(WeChatRefundResponse response) {
        if (!response.isReturnSuccess()) {
            return "通信失败：" + response.getReturnMsg();
        }
        
        if (!response.isResultSuccess()) {
            return "业务失败：" + response.getErrCodeDes();
        }
        
        if (response.getRefundId() != null) {
            return "退款成功，退款单号：" + response.getRefundId();
        }
        
        return "退款处理中";
    }
}