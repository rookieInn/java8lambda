package com.extrigger.wechat.service;

import com.extrigger.wechat.config.WeChatPayConfig;
import com.extrigger.wechat.model.*;
import com.extrigger.wechat.util.WeChatPayUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

/**
 * 微信支付服务类
 * @author extrigger
 */
@Service
public class WeChatPayService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeChatPayService.class);
    
    @Autowired
    private WeChatPayConfig weChatPayConfig;
    
    /**
     * 统一下单
     * @param description 商品描述
     * @param outTradeNo 商户订单号
     * @param totalAmount 订单金额（分）
     * @param openid 用户openid
     * @return 支付响应
     */
    public PayResponse createOrder(String description, String outTradeNo, Integer totalAmount, String openid) {
        try {
            // 构建请求参数
            PayRequest payRequest = new PayRequest();
            payRequest.setDescription(description);
            payRequest.setOutTradeNo(outTradeNo);
            payRequest.setAmount(new PayRequest.Amount(totalAmount));
            payRequest.setPayer(new PayRequest.Payer(openid));
            payRequest.setNotifyUrl(weChatPayConfig.getNotifyUrl());
            
            // 构建请求体
            String requestBody = WeChatPayUtil.toJsonString(payRequest);
            logger.info("微信支付下单请求: {}", requestBody);
            
            // 构建签名
            long timestamp = WeChatPayUtil.getCurrentTimestamp();
            String nonceStr = WeChatPayUtil.generateNonceStr();
            URI uri = URI.create(weChatPayConfig.getUnifiedOrderUrl());
            String url = uri.getPath();
            if (uri.getQuery() != null) {
                url += "?" + uri.getQuery();
            }
            
            String signString = WeChatPayUtil.buildSignString("POST", url, timestamp, nonceStr, requestBody);
            String signature = WeChatPayUtil.sign(signString, weChatPayConfig.getPrivateKeyPath());
            
            // 构建Authorization头
            String authorizationHeader = WeChatPayUtil.buildAuthorizationHeader(
                    weChatPayConfig.getMchId(),
                    weChatPayConfig.getSerialNo(),
                    timestamp,
                    nonceStr,
                    signature
            );
            
            // 发送请求
            String responseBody = WeChatPayUtil.sendPostRequest(weChatPayConfig.getUnifiedOrderUrl(), requestBody, authorizationHeader);
            logger.info("微信支付下单响应: {}", responseBody);
            
            return WeChatPayUtil.fromJsonString(responseBody, PayResponse.class);
            
        } catch (Exception e) {
            logger.error("微信支付下单失败", e);
            PayResponse errorResponse = new PayResponse();
            errorResponse.setCode("SYSTEM_ERROR");
            errorResponse.setMessage("系统异常: " + e.getMessage());
            return errorResponse;
        }
    }
    
    /**
     * 查询订单
     * @param outTradeNo 商户订单号
     * @return 订单查询响应
     */
    public OrderQueryResponse queryOrder(String outTradeNo) {
        try {
            // 构建请求URL
            String url = weChatPayConfig.getQueryOrderUrl() + "/" + outTradeNo + "?mchid=" + weChatPayConfig.getMchId();
            
            // 构建签名
            long timestamp = WeChatPayUtil.getCurrentTimestamp();
            String nonceStr = WeChatPayUtil.generateNonceStr();
            URI uri = URI.create(url);
            String urlPath = uri.getPath();
            if (uri.getQuery() != null) {
                urlPath += "?" + uri.getQuery();
            }
            
            String signString = WeChatPayUtil.buildSignString("GET", urlPath, timestamp, nonceStr, "");
            String signature = WeChatPayUtil.sign(signString, weChatPayConfig.getPrivateKeyPath());
            
            // 构建Authorization头
            String authorizationHeader = WeChatPayUtil.buildAuthorizationHeader(
                    weChatPayConfig.getMchId(),
                    weChatPayConfig.getSerialNo(),
                    timestamp,
                    nonceStr,
                    signature
            );
            
            // 发送请求
            String responseBody = WeChatPayUtil.sendGetRequest(url, authorizationHeader);
            logger.info("微信支付查询订单响应: {}", responseBody);
            
            return WeChatPayUtil.fromJsonString(responseBody, OrderQueryResponse.class);
            
        } catch (Exception e) {
            logger.error("查询微信支付订单失败", e);
            return null;
        }
    }
    
    /**
     * 申请退款
     * @param outTradeNo 商户订单号
     * @param outRefundNo 商户退款单号
     * @param refundAmount 退款金额（分）
     * @param totalAmount 原订单金额（分）
     * @param reason 退款原因
     * @return 退款响应
     */
    public RefundResponse refund(String outTradeNo, String outRefundNo, Integer refundAmount, Integer totalAmount, String reason) {
        try {
            // 构建请求参数
            RefundRequest refundRequest = new RefundRequest();
            refundRequest.setOutTradeNo(outTradeNo);
            refundRequest.setOutRefundNo(outRefundNo);
            refundRequest.setReason(reason);
            refundRequest.setAmount(new RefundRequest.Amount(refundAmount, totalAmount));
            refundRequest.setNotifyUrl(weChatPayConfig.getRefundNotifyUrl());
            
            // 构建请求体
            String requestBody = WeChatPayUtil.toJsonString(refundRequest);
            logger.info("微信支付退款请求: {}", requestBody);
            
            // 构建签名
            long timestamp = WeChatPayUtil.getCurrentTimestamp();
            String nonceStr = WeChatPayUtil.generateNonceStr();
            URI uri = URI.create(weChatPayConfig.getRefundUrl());
            String url = uri.getPath();
            if (uri.getQuery() != null) {
                url += "?" + uri.getQuery();
            }
            
            String signString = WeChatPayUtil.buildSignString("POST", url, timestamp, nonceStr, requestBody);
            String signature = WeChatPayUtil.sign(signString, weChatPayConfig.getPrivateKeyPath());
            
            // 构建Authorization头
            String authorizationHeader = WeChatPayUtil.buildAuthorizationHeader(
                    weChatPayConfig.getMchId(),
                    weChatPayConfig.getSerialNo(),
                    timestamp,
                    nonceStr,
                    signature
            );
            
            // 发送请求
            String responseBody = WeChatPayUtil.sendPostRequest(weChatPayConfig.getRefundUrl(), requestBody, authorizationHeader);
            logger.info("微信支付退款响应: {}", responseBody);
            
            return WeChatPayUtil.fromJsonString(responseBody, RefundResponse.class);
            
        } catch (Exception e) {
            logger.error("微信支付退款失败", e);
            RefundResponse errorResponse = new RefundResponse();
            errorResponse.setCode("SYSTEM_ERROR");
            errorResponse.setMessage("系统异常: " + e.getMessage());
            return errorResponse;
        }
    }
    
    /**
     * 查询退款
     * @param outRefundNo 商户退款单号
     * @return 退款查询响应
     */
    public RefundResponse queryRefund(String outRefundNo) {
        try {
            // 构建请求URL
            String url = weChatPayConfig.getQueryRefundUrl() + "/" + outRefundNo;
            
            // 构建签名
            long timestamp = WeChatPayUtil.getCurrentTimestamp();
            String nonceStr = WeChatPayUtil.generateNonceStr();
            URI uri = URI.create(url);
            String urlPath = uri.getPath();
            if (uri.getQuery() != null) {
                urlPath += "?" + uri.getQuery();
            }
            
            String signString = WeChatPayUtil.buildSignString("GET", urlPath, timestamp, nonceStr, "");
            String signature = WeChatPayUtil.sign(signString, weChatPayConfig.getPrivateKeyPath());
            
            // 构建Authorization头
            String authorizationHeader = WeChatPayUtil.buildAuthorizationHeader(
                    weChatPayConfig.getMchId(),
                    weChatPayConfig.getSerialNo(),
                    timestamp,
                    nonceStr,
                    signature
            );
            
            // 发送请求
            String responseBody = WeChatPayUtil.sendGetRequest(url, authorizationHeader);
            logger.info("微信支付查询退款响应: {}", responseBody);
            
            return WeChatPayUtil.fromJsonString(responseBody, RefundResponse.class);
            
        } catch (Exception e) {
            logger.error("查询微信支付退款失败", e);
            return null;
        }
    }
    
    /**
     * 生成微信小程序支付参数
     * @param prepayId 预支付ID
     * @return 支付参数
     * @throws Exception 异常
     */
    public WeChatPayUtil.WeChatPayParams generatePayParams(String prepayId) throws Exception {
        return WeChatPayUtil.generateMiniProgramPayParams(weChatPayConfig.getAppId(), prepayId, weChatPayConfig.getPrivateKeyPath());
    }
    
    /**
     * 验证支付回调签名
     * @param timestamp 时间戳
     * @param nonceStr 随机字符串
     * @param body 回调体
     * @param signature 签名
     * @return 验证结果
     */
    public boolean verifyNotifySignature(String timestamp, String nonceStr, String body, String signature) {
        return WeChatPayUtil.verifySignature(timestamp, nonceStr, body, signature, weChatPayConfig.getCertificatePath());
    }
}