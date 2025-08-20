package com.example.wechatpay.service;

import com.alibaba.fastjson.JSON;
import com.example.wechatpay.config.WeChatPayConfig;
import com.example.wechatpay.constant.WeChatPayConstants;
import com.example.wechatpay.model.request.RefundRequest;
import com.example.wechatpay.model.request.UnifiedOrderRequest;
import com.example.wechatpay.model.response.PaymentNotifyResponse;
import com.example.wechatpay.model.response.RefundResponse;
import com.example.wechatpay.model.response.UnifiedOrderResponse;
import com.example.wechatpay.util.HttpClientUtil;
import com.example.wechatpay.util.SignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付服务类
 * 提供支付、退款、查询等功能
 */
@Service
public class WeChatPayService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeChatPayService.class);
    
    @Autowired
    private WeChatPayConfig config;
    
    private PrivateKey privateKey;
    
    /**
     * 初始化，加载私钥
     */
    @PostConstruct
    public void init() {
        try {
            // 如果配置了私钥内容，直接使用
            if (config.getPrivateKey() != null && !config.getPrivateKey().isEmpty()) {
                this.privateKey = SignatureUtil.loadPrivateKey(config.getPrivateKey());
            }
            // 否则从文件加载
            else if (config.getPrivateKeyPath() != null && !config.getPrivateKeyPath().isEmpty()) {
                // 这里需要读取文件内容，简化起见，假设已经读取到privateKeyContent
                String privateKeyContent = ""; // 从文件读取
                this.privateKey = SignatureUtil.loadPrivateKey(privateKeyContent);
            }
            logger.info("微信支付服务初始化成功");
        } catch (Exception e) {
            logger.error("微信支付服务初始化失败", e);
        }
    }
    
    /**
     * JSAPI支付（公众号/小程序支付）
     * 
     * @param request 统一下单请求
     * @return 预支付交易会话标识
     */
    public String jsapiPay(UnifiedOrderRequest request) {
        try {
            // 设置必要参数
            request.setAppId(config.getAppId());
            request.setMchId(config.getMchId());
            request.setNotifyUrl(config.getNotifyUrl());
            
            // 构建请求URL
            String url = WeChatPayConstants.DOMAIN_API_V3 + WeChatPayConstants.JSAPI_PAY_URL;
            
            // 发送请求
            String requestBody = JSON.toJSONString(request);
            String response = HttpClientUtil.doPostWithSign(url, requestBody, 
                    config.getMchId(), config.getMchSerialNo(), privateKey);
            
            // 解析响应
            UnifiedOrderResponse orderResponse = HttpClientUtil.parseResponse(response, UnifiedOrderResponse.class);
            if (orderResponse != null) {
                logger.info("JSAPI支付下单成功，prepay_id: {}", orderResponse.getPrepayId());
                return orderResponse.getPrepayId();
            }
            
            logger.error("JSAPI支付下单失败，响应: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("JSAPI支付异常", e);
            return null;
        }
    }
    
    /**
     * Native支付（扫码支付）
     * 
     * @param request 统一下单请求
     * @return 二维码链接
     */
    public String nativePay(UnifiedOrderRequest request) {
        try {
            // 设置必要参数
            request.setAppId(config.getAppId());
            request.setMchId(config.getMchId());
            request.setNotifyUrl(config.getNotifyUrl());
            
            // 构建请求URL
            String url = WeChatPayConstants.DOMAIN_API_V3 + WeChatPayConstants.NATIVE_PAY_URL;
            
            // 发送请求
            String requestBody = JSON.toJSONString(request);
            String response = HttpClientUtil.doPostWithSign(url, requestBody, 
                    config.getMchId(), config.getMchSerialNo(), privateKey);
            
            // 解析响应
            UnifiedOrderResponse orderResponse = HttpClientUtil.parseResponse(response, UnifiedOrderResponse.class);
            if (orderResponse != null) {
                logger.info("Native支付下单成功，code_url: {}", orderResponse.getCodeUrl());
                return orderResponse.getCodeUrl();
            }
            
            logger.error("Native支付下单失败，响应: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("Native支付异常", e);
            return null;
        }
    }
    
    /**
     * APP支付
     * 
     * @param request 统一下单请求
     * @return 预支付交易会话标识
     */
    public String appPay(UnifiedOrderRequest request) {
        try {
            // 设置必要参数
            request.setAppId(config.getAppId());
            request.setMchId(config.getMchId());
            request.setNotifyUrl(config.getNotifyUrl());
            
            // 构建请求URL
            String url = WeChatPayConstants.DOMAIN_API_V3 + WeChatPayConstants.APP_PAY_URL;
            
            // 发送请求
            String requestBody = JSON.toJSONString(request);
            String response = HttpClientUtil.doPostWithSign(url, requestBody, 
                    config.getMchId(), config.getMchSerialNo(), privateKey);
            
            // 解析响应
            UnifiedOrderResponse orderResponse = HttpClientUtil.parseResponse(response, UnifiedOrderResponse.class);
            if (orderResponse != null) {
                logger.info("APP支付下单成功，prepay_id: {}", orderResponse.getPrepayId());
                return orderResponse.getPrepayId();
            }
            
            logger.error("APP支付下单失败，响应: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("APP支付异常", e);
            return null;
        }
    }
    
    /**
     * H5支付
     * 
     * @param request 统一下单请求
     * @return 支付跳转链接
     */
    public String h5Pay(UnifiedOrderRequest request) {
        try {
            // 设置必要参数
            request.setAppId(config.getAppId());
            request.setMchId(config.getMchId());
            request.setNotifyUrl(config.getNotifyUrl());
            
            // 构建请求URL
            String url = WeChatPayConstants.DOMAIN_API_V3 + WeChatPayConstants.H5_PAY_URL;
            
            // 发送请求
            String requestBody = JSON.toJSONString(request);
            String response = HttpClientUtil.doPostWithSign(url, requestBody, 
                    config.getMchId(), config.getMchSerialNo(), privateKey);
            
            // 解析响应
            UnifiedOrderResponse orderResponse = HttpClientUtil.parseResponse(response, UnifiedOrderResponse.class);
            if (orderResponse != null) {
                logger.info("H5支付下单成功，h5_url: {}", orderResponse.getH5Url());
                return orderResponse.getH5Url();
            }
            
            logger.error("H5支付下单失败，响应: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("H5支付异常", e);
            return null;
        }
    }
    
    /**
     * 申请退款
     * 
     * @param request 退款请求
     * @return 退款响应
     */
    public RefundResponse refund(RefundRequest request) {
        try {
            // 设置退款通知地址
            if (request.getNotifyUrl() == null) {
                request.setNotifyUrl(config.getRefundNotifyUrl());
            }
            
            // 构建请求URL
            String url = WeChatPayConstants.DOMAIN_API_V3 + WeChatPayConstants.REFUND_URL;
            
            // 发送请求
            String requestBody = JSON.toJSONString(request);
            String response = HttpClientUtil.doPostWithSign(url, requestBody, 
                    config.getMchId(), config.getMchSerialNo(), privateKey);
            
            // 解析响应
            RefundResponse refundResponse = HttpClientUtil.parseResponse(response, RefundResponse.class);
            if (refundResponse != null) {
                logger.info("退款申请成功，退款单号: {}, 状态: {}", 
                        refundResponse.getOutRefundNo(), refundResponse.getStatus());
                return refundResponse;
            }
            
            logger.error("退款申请失败，响应: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("退款申请异常", e);
            return null;
        }
    }
    
    /**
     * 查询订单（通过微信支付订单号）
     * 
     * @param transactionId 微信支付订单号
     * @return 订单信息
     */
    public Map<String, Object> queryOrderByTransactionId(String transactionId) {
        try {
            // 构建请求URL
            String url = WeChatPayConstants.DOMAIN_API_V3 + 
                    WeChatPayConstants.QUERY_ORDER_BY_ID.replace("{transaction_id}", transactionId);
            url += "?mchid=" + config.getMchId();
            
            // 发送请求
            String response = HttpClientUtil.doGetWithSign(url, 
                    config.getMchId(), config.getMchSerialNo(), privateKey);
            
            // 解析响应
            Map<String, Object> result = JSON.parseObject(response, Map.class);
            if (result != null) {
                logger.info("查询订单成功，订单号: {}, 状态: {}", 
                        transactionId, result.get("trade_state"));
                return result;
            }
            
            logger.error("查询订单失败，响应: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("查询订单异常", e);
            return null;
        }
    }
    
    /**
     * 查询订单（通过商户订单号）
     * 
     * @param outTradeNo 商户订单号
     * @return 订单信息
     */
    public Map<String, Object> queryOrderByOutTradeNo(String outTradeNo) {
        try {
            // 构建请求URL
            String url = WeChatPayConstants.DOMAIN_API_V3 + 
                    WeChatPayConstants.QUERY_ORDER_BY_OUT_TRADE_NO.replace("{out_trade_no}", outTradeNo);
            url += "?mchid=" + config.getMchId();
            
            // 发送请求
            String response = HttpClientUtil.doGetWithSign(url, 
                    config.getMchId(), config.getMchSerialNo(), privateKey);
            
            // 解析响应
            Map<String, Object> result = JSON.parseObject(response, Map.class);
            if (result != null) {
                logger.info("查询订单成功，商户订单号: {}, 状态: {}", 
                        outTradeNo, result.get("trade_state"));
                return result;
            }
            
            logger.error("查询订单失败，响应: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("查询订单异常", e);
            return null;
        }
    }
    
    /**
     * 查询退款
     * 
     * @param outRefundNo 商户退款单号
     * @return 退款信息
     */
    public RefundResponse queryRefund(String outRefundNo) {
        try {
            // 构建请求URL
            String url = WeChatPayConstants.DOMAIN_API_V3 + 
                    WeChatPayConstants.QUERY_REFUND_URL.replace("{out_refund_no}", outRefundNo);
            
            // 发送请求
            String response = HttpClientUtil.doGetWithSign(url, 
                    config.getMchId(), config.getMchSerialNo(), privateKey);
            
            // 解析响应
            RefundResponse refundResponse = HttpClientUtil.parseResponse(response, RefundResponse.class);
            if (refundResponse != null) {
                logger.info("查询退款成功，退款单号: {}, 状态: {}", 
                        outRefundNo, refundResponse.getStatus());
                return refundResponse;
            }
            
            logger.error("查询退款失败，响应: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("查询退款异常", e);
            return null;
        }
    }
    
    /**
     * 关闭订单
     * 
     * @param outTradeNo 商户订单号
     * @return 是否成功
     */
    public boolean closeOrder(String outTradeNo) {
        try {
            // 构建请求URL
            String url = WeChatPayConstants.DOMAIN_API_V3 + 
                    WeChatPayConstants.CLOSE_ORDER_URL.replace("{out_trade_no}", outTradeNo);
            
            // 构建请求体
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("mchid", config.getMchId());
            String requestBody = JSON.toJSONString(requestMap);
            
            // 发送请求
            String response = HttpClientUtil.doPostWithSign(url, requestBody, 
                    config.getMchId(), config.getMchSerialNo(), privateKey);
            
            logger.info("关闭订单成功，商户订单号: {}", outTradeNo);
            return true;
        } catch (Exception e) {
            logger.error("关闭订单异常", e);
            return false;
        }
    }
    
    /**
     * 处理支付通知
     * 
     * @param notifyData 通知数据
     * @return 解密后的支付结果
     */
    public PaymentNotifyResponse.DecryptedResource handlePaymentNotify(String notifyData) {
        try {
            // 解析通知数据
            PaymentNotifyResponse notify = JSON.parseObject(notifyData, PaymentNotifyResponse.class);
            
            // 解密资源数据
            String decryptedData = SignatureUtil.decryptAesGcm(
                    notify.getResource().getAssociatedData(),
                    notify.getResource().getNonce(),
                    notify.getResource().getCiphertext(),
                    config.getApiV3Key()
            );
            
            // 解析解密后的数据
            PaymentNotifyResponse.DecryptedResource resource = 
                    JSON.parseObject(decryptedData, PaymentNotifyResponse.DecryptedResource.class);
            
            logger.info("处理支付通知成功，订单号: {}, 交易状态: {}", 
                    resource.getOutTradeNo(), resource.getTradeState());
            
            return resource;
        } catch (Exception e) {
            logger.error("处理支付通知异常", e);
            return null;
        }
    }
    
    /**
     * 生成JSAPI支付参数
     * 用于前端调起支付
     * 
     * @param prepayId 预支付交易会话标识
     * @return 支付参数
     */
    public Map<String, String> generateJsapiPayParams(String prepayId) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("appId", config.getAppId());
            params.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            params.put("nonceStr", SignatureUtil.generateNonceStr());
            params.put("package", "prepay_id=" + prepayId);
            params.put("signType", "RSA");
            
            // 生成签名
            String message = params.get("appId") + "\n"
                    + params.get("timeStamp") + "\n"
                    + params.get("nonceStr") + "\n"
                    + params.get("package") + "\n";
            
            String signature = SignatureUtil.sign(message, privateKey);
            params.put("paySign", signature);
            
            return params;
        } catch (Exception e) {
            logger.error("生成JSAPI支付参数异常", e);
            return null;
        }
    }
    
    /**
     * 生成APP支付参数
     * 用于APP调起支付
     * 
     * @param prepayId 预支付交易会话标识
     * @return 支付参数
     */
    public Map<String, String> generateAppPayParams(String prepayId) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("appid", config.getAppId());
            params.put("partnerid", config.getMchId());
            params.put("prepayid", prepayId);
            params.put("package", "Sign=WXPay");
            params.put("noncestr", SignatureUtil.generateNonceStr());
            params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            
            // 生成签名
            String message = params.get("appid") + "\n"
                    + params.get("timestamp") + "\n"
                    + params.get("noncestr") + "\n"
                    + params.get("prepayid") + "\n";
            
            String signature = SignatureUtil.sign(message, privateKey);
            params.put("sign", signature);
            
            return params;
        } catch (Exception e) {
            logger.error("生成APP支付参数异常", e);
            return null;
        }
    }
}