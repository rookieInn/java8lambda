package com.example.wechatpay.controller;

import com.example.wechatpay.model.request.RefundRequest;
import com.example.wechatpay.model.request.UnifiedOrderRequest;
import com.example.wechatpay.model.response.PaymentNotifyResponse;
import com.example.wechatpay.model.response.RefundResponse;
import com.example.wechatpay.service.WeChatPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信支付控制器
 * 提供支付、退款等接口
 */
@RestController
@RequestMapping("/api/wechat-pay")
public class WeChatPayController {
    
    private static final Logger logger = LoggerFactory.getLogger(WeChatPayController.class);
    
    @Autowired
    private WeChatPayService weChatPayService;
    
    /**
     * 创建JSAPI支付订单（公众号/小程序支付）
     * 
     * @param openId 用户openId
     * @param amount 金额（分）
     * @param description 商品描述
     * @return 支付参数
     */
    @PostMapping("/jsapi/create")
    public Map<String, Object> createJsapiOrder(
            @RequestParam String openId,
            @RequestParam Integer amount,
            @RequestParam String description) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 生成商户订单号
            String outTradeNo = generateOutTradeNo();
            
            // 构建下单请求
            UnifiedOrderRequest request = UnifiedOrderRequest.builder()
                    .outTradeNo(outTradeNo)
                    .description(description)
                    .amount(UnifiedOrderRequest.Amount.builder()
                            .total(amount)
                            .currency("CNY")
                            .build())
                    .payer(UnifiedOrderRequest.Payer.builder()
                            .openid(openId)
                            .build())
                    .build();
            
            // 调用支付服务
            String prepayId = weChatPayService.jsapiPay(request);
            
            if (prepayId != null) {
                // 生成支付参数
                Map<String, String> payParams = weChatPayService.generateJsapiPayParams(prepayId);
                
                result.put("success", true);
                result.put("outTradeNo", outTradeNo);
                result.put("payParams", payParams);
                result.put("message", "订单创建成功");
            } else {
                result.put("success", false);
                result.put("message", "订单创建失败");
            }
        } catch (Exception e) {
            logger.error("创建JSAPI订单异常", e);
            result.put("success", false);
            result.put("message", "系统异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 创建Native支付订单（扫码支付）
     * 
     * @param amount 金额（分）
     * @param description 商品描述
     * @return 二维码链接
     */
    @PostMapping("/native/create")
    public Map<String, Object> createNativeOrder(
            @RequestParam Integer amount,
            @RequestParam String description) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 生成商户订单号
            String outTradeNo = generateOutTradeNo();
            
            // 构建下单请求
            UnifiedOrderRequest request = UnifiedOrderRequest.builder()
                    .outTradeNo(outTradeNo)
                    .description(description)
                    .amount(UnifiedOrderRequest.Amount.builder()
                            .total(amount)
                            .currency("CNY")
                            .build())
                    .build();
            
            // 调用支付服务
            String codeUrl = weChatPayService.nativePay(request);
            
            if (codeUrl != null) {
                result.put("success", true);
                result.put("outTradeNo", outTradeNo);
                result.put("codeUrl", codeUrl);
                result.put("message", "订单创建成功");
            } else {
                result.put("success", false);
                result.put("message", "订单创建失败");
            }
        } catch (Exception e) {
            logger.error("创建Native订单异常", e);
            result.put("success", false);
            result.put("message", "系统异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 创建APP支付订单
     * 
     * @param amount 金额（分）
     * @param description 商品描述
     * @return APP支付参数
     */
    @PostMapping("/app/create")
    public Map<String, Object> createAppOrder(
            @RequestParam Integer amount,
            @RequestParam String description) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 生成商户订单号
            String outTradeNo = generateOutTradeNo();
            
            // 构建下单请求
            UnifiedOrderRequest request = UnifiedOrderRequest.builder()
                    .outTradeNo(outTradeNo)
                    .description(description)
                    .amount(UnifiedOrderRequest.Amount.builder()
                            .total(amount)
                            .currency("CNY")
                            .build())
                    .build();
            
            // 调用支付服务
            String prepayId = weChatPayService.appPay(request);
            
            if (prepayId != null) {
                // 生成APP支付参数
                Map<String, String> payParams = weChatPayService.generateAppPayParams(prepayId);
                
                result.put("success", true);
                result.put("outTradeNo", outTradeNo);
                result.put("payParams", payParams);
                result.put("message", "订单创建成功");
            } else {
                result.put("success", false);
                result.put("message", "订单创建失败");
            }
        } catch (Exception e) {
            logger.error("创建APP订单异常", e);
            result.put("success", false);
            result.put("message", "系统异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 创建H5支付订单
     * 
     * @param amount 金额（分）
     * @param description 商品描述
     * @param clientIp 客户端IP
     * @return H5支付链接
     */
    @PostMapping("/h5/create")
    public Map<String, Object> createH5Order(
            @RequestParam Integer amount,
            @RequestParam String description,
            @RequestParam String clientIp) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 生成商户订单号
            String outTradeNo = generateOutTradeNo();
            
            // 构建下单请求
            UnifiedOrderRequest request = UnifiedOrderRequest.builder()
                    .outTradeNo(outTradeNo)
                    .description(description)
                    .amount(UnifiedOrderRequest.Amount.builder()
                            .total(amount)
                            .currency("CNY")
                            .build())
                    .sceneInfo(UnifiedOrderRequest.SceneInfo.builder()
                            .payerClientIp(clientIp)
                            .h5Info(UnifiedOrderRequest.H5Info.builder()
                                    .type("Wap")
                                    .build())
                            .build())
                    .build();
            
            // 调用支付服务
            String h5Url = weChatPayService.h5Pay(request);
            
            if (h5Url != null) {
                result.put("success", true);
                result.put("outTradeNo", outTradeNo);
                result.put("h5Url", h5Url);
                result.put("message", "订单创建成功");
            } else {
                result.put("success", false);
                result.put("message", "订单创建失败");
            }
        } catch (Exception e) {
            logger.error("创建H5订单异常", e);
            result.put("success", false);
            result.put("message", "系统异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 申请退款
     * 
     * @param outTradeNo 商户订单号
     * @param refundAmount 退款金额（分）
     * @param totalAmount 原订单金额（分）
     * @param reason 退款原因
     * @return 退款结果
     */
    @PostMapping("/refund")
    public Map<String, Object> refund(
            @RequestParam String outTradeNo,
            @RequestParam Integer refundAmount,
            @RequestParam Integer totalAmount,
            @RequestParam(required = false) String reason) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 生成商户退款单号
            String outRefundNo = generateOutRefundNo();
            
            // 构建退款请求
            RefundRequest request = RefundRequest.builder()
                    .outTradeNo(outTradeNo)
                    .outRefundNo(outRefundNo)
                    .reason(reason != null ? reason : "用户申请退款")
                    .amount(RefundRequest.RefundAmount.builder()
                            .refund(refundAmount)
                            .total(totalAmount)
                            .currency("CNY")
                            .build())
                    .build();
            
            // 调用退款服务
            RefundResponse refundResponse = weChatPayService.refund(request);
            
            if (refundResponse != null) {
                result.put("success", true);
                result.put("outRefundNo", outRefundNo);
                result.put("refundId", refundResponse.getRefundId());
                result.put("status", refundResponse.getStatus());
                result.put("message", "退款申请成功");
            } else {
                result.put("success", false);
                result.put("message", "退款申请失败");
            }
        } catch (Exception e) {
            logger.error("申请退款异常", e);
            result.put("success", false);
            result.put("message", "系统异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 查询订单
     * 
     * @param outTradeNo 商户订单号
     * @return 订单信息
     */
    @GetMapping("/order/query")
    public Map<String, Object> queryOrder(@RequestParam String outTradeNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> orderInfo = weChatPayService.queryOrderByOutTradeNo(outTradeNo);
            
            if (orderInfo != null) {
                result.put("success", true);
                result.put("data", orderInfo);
                result.put("message", "查询成功");
            } else {
                result.put("success", false);
                result.put("message", "订单不存在");
            }
        } catch (Exception e) {
            logger.error("查询订单异常", e);
            result.put("success", false);
            result.put("message", "系统异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 查询退款
     * 
     * @param outRefundNo 商户退款单号
     * @return 退款信息
     */
    @GetMapping("/refund/query")
    public Map<String, Object> queryRefund(@RequestParam String outRefundNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            RefundResponse refundInfo = weChatPayService.queryRefund(outRefundNo);
            
            if (refundInfo != null) {
                result.put("success", true);
                result.put("data", refundInfo);
                result.put("message", "查询成功");
            } else {
                result.put("success", false);
                result.put("message", "退款单不存在");
            }
        } catch (Exception e) {
            logger.error("查询退款异常", e);
            result.put("success", false);
            result.put("message", "系统异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 关闭订单
     * 
     * @param outTradeNo 商户订单号
     * @return 操作结果
     */
    @PostMapping("/order/close")
    public Map<String, Object> closeOrder(@RequestParam String outTradeNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = weChatPayService.closeOrder(outTradeNo);
            
            result.put("success", success);
            result.put("message", success ? "订单关闭成功" : "订单关闭失败");
        } catch (Exception e) {
            logger.error("关闭订单异常", e);
            result.put("success", false);
            result.put("message", "系统异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 支付通知回调
     * 微信支付成功后会调用此接口
     * 
     * @param request HTTP请求
     * @return 响应
     */
    @PostMapping("/notify/payment")
    public Map<String, String> paymentNotify(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // 读取请求体
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            String notifyData = sb.toString();
            
            logger.info("收到支付通知: {}", notifyData);
            
            // 处理支付通知
            PaymentNotifyResponse.DecryptedResource resource = 
                    weChatPayService.handlePaymentNotify(notifyData);
            
            if (resource != null) {
                // 处理业务逻辑
                String outTradeNo = resource.getOutTradeNo();
                String tradeState = resource.getTradeState();
                
                if ("SUCCESS".equals(tradeState)) {
                    // 支付成功，更新订单状态
                    logger.info("订单支付成功: {}", outTradeNo);
                    // TODO: 更新订单状态，发货等业务逻辑
                }
                
                response.put("code", "SUCCESS");
                response.put("message", "成功");
            } else {
                response.put("code", "FAIL");
                response.put("message", "处理失败");
            }
        } catch (Exception e) {
            logger.error("处理支付通知异常", e);
            response.put("code", "FAIL");
            response.put("message", "系统异常");
        }
        
        return response;
    }
    
    /**
     * 退款通知回调
     * 退款成功后会调用此接口
     * 
     * @param request HTTP请求
     * @return 响应
     */
    @PostMapping("/notify/refund")
    public Map<String, String> refundNotify(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // 读取请求体
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            String notifyData = sb.toString();
            
            logger.info("收到退款通知: {}", notifyData);
            
            // TODO: 处理退款通知，解密数据，更新退款状态
            
            response.put("code", "SUCCESS");
            response.put("message", "成功");
        } catch (Exception e) {
            logger.error("处理退款通知异常", e);
            response.put("code", "FAIL");
            response.put("message", "系统异常");
        }
        
        return response;
    }
    
    /**
     * 生成商户订单号
     * 
     * @return 商户订单号
     */
    private String generateOutTradeNo() {
        // 格式：时间戳 + 随机数
        return System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * 生成商户退款单号
     * 
     * @return 商户退款单号
     */
    private String generateOutRefundNo() {
        // 格式：R + 时间戳 + 随机数
        return "R" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
}