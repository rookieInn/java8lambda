package com.extrigger.wechat.controller;

import com.extrigger.wechat.model.*;
import com.extrigger.wechat.service.WeChatPayService;
import com.extrigger.wechat.util.WeChatPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信支付控制器
 * @author extrigger
 */
@RestController
@RequestMapping("/api/wechat/pay")
public class WeChatPayController {
    
    private static final Logger logger = LoggerFactory.getLogger(WeChatPayController.class);
    
    @Autowired
    private WeChatPayService weChatPayService;
    
    /**
     * 创建支付订单
     * @param request 支付请求
     * @return 支付响应
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayOrder(@RequestBody CreatePayOrderRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 生成商户订单号（如果没有提供）
            String outTradeNo = request.getOutTradeNo();
            if (outTradeNo == null || outTradeNo.trim().isEmpty()) {
                outTradeNo = "ORDER_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
            }
            
            // 调用统一下单
            PayResponse payResponse = weChatPayService.createOrder(
                    request.getDescription(),
                    outTradeNo,
                    request.getTotalAmount(),
                    request.getOpenid()
            );
            
            if (payResponse.getPrepayId() != null) {
                // 生成支付参数
                WeChatPayUtil.WeChatPayParams payParams = weChatPayService.generatePayParams(payResponse.getPrepayId());
                
                result.put("success", true);
                result.put("outTradeNo", outTradeNo);
                result.put("prepayId", payResponse.getPrepayId());
                result.put("payParams", payParams);
                result.put("message", "订单创建成功");
                
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("code", payResponse.getCode());
                result.put("message", payResponse.getMessage());
                result.put("detail", payResponse.getDetail());
                
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            logger.error("创建支付订单失败", e);
            result.put("success", false);
            result.put("message", "创建订单失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 查询订单状态
     * @param outTradeNo 商户订单号
     * @return 订单状态
     */
    @GetMapping("/query/{outTradeNo}")
    public ResponseEntity<Map<String, Object>> queryOrder(@PathVariable String outTradeNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            OrderQueryResponse queryResponse = weChatPayService.queryOrder(outTradeNo);
            
            if (queryResponse != null) {
                result.put("success", true);
                result.put("data", queryResponse);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "订单不存在或查询失败");
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            logger.error("查询订单失败", e);
            result.put("success", false);
            result.put("message", "查询订单失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 申请退款
     * @param request 退款请求
     * @return 退款响应
     */
    @PostMapping("/refund")
    public ResponseEntity<Map<String, Object>> refund(@RequestBody CreateRefundRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 生成商户退款单号（如果没有提供）
            String outRefundNo = request.getOutRefundNo();
            if (outRefundNo == null || outRefundNo.trim().isEmpty()) {
                outRefundNo = "REFUND_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
            }
            
            // 调用退款接口
            RefundResponse refundResponse = weChatPayService.refund(
                    request.getOutTradeNo(),
                    outRefundNo,
                    request.getRefundAmount(),
                    request.getTotalAmount(),
                    request.getReason()
            );
            
            if (refundResponse.getRefundId() != null) {
                result.put("success", true);
                result.put("data", refundResponse);
                result.put("message", "退款申请成功");
                
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("code", refundResponse.getCode());
                result.put("message", refundResponse.getMessage());
                
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            logger.error("申请退款失败", e);
            result.put("success", false);
            result.put("message", "申请退款失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 查询退款状态
     * @param outRefundNo 商户退款单号
     * @return 退款状态
     */
    @GetMapping("/refund/query/{outRefundNo}")
    public ResponseEntity<Map<String, Object>> queryRefund(@PathVariable String outRefundNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            RefundResponse refundResponse = weChatPayService.queryRefund(outRefundNo);
            
            if (refundResponse != null) {
                result.put("success", true);
                result.put("data", refundResponse);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "退款单不存在或查询失败");
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            logger.error("查询退款失败", e);
            result.put("success", false);
            result.put("message", "查询退款失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 支付结果通知回调
     * @param request HTTP请求
     * @return 处理结果
     */
    @PostMapping("/notify")
    public ResponseEntity<Map<String, Object>> payNotify(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取请求头
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonceStr = request.getHeader("Wechatpay-Nonce");
            String signature = request.getHeader("Wechatpay-Signature");
            String serialNo = request.getHeader("Wechatpay-Serial");
            
            // 读取请求体
            String body = getRequestBody(request);
            logger.info("微信支付回调通知: timestamp={}, nonceStr={}, signature={}, serialNo={}, body={}", 
                    timestamp, nonceStr, signature, serialNo, body);
            
            // 验证签名
            boolean isValid = weChatPayService.verifyNotifySignature(timestamp, nonceStr, body, signature);
            
            if (isValid) {
                // 处理支付成功逻辑
                // TODO: 根据业务需求处理支付成功后的逻辑
                logger.info("支付回调验证成功，开始处理业务逻辑");
                
                result.put("code", "SUCCESS");
                result.put("message", "成功");
                return ResponseEntity.ok(result);
            } else {
                logger.warn("支付回调签名验证失败");
                result.put("code", "FAIL");
                result.put("message", "签名验证失败");
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            logger.error("处理支付回调失败", e);
            result.put("code", "FAIL");
            result.put("message", "处理失败");
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 退款结果通知回调
     * @param request HTTP请求
     * @return 处理结果
     */
    @PostMapping("/refund/notify")
    public ResponseEntity<Map<String, Object>> refundNotify(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取请求头
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonceStr = request.getHeader("Wechatpay-Nonce");
            String signature = request.getHeader("Wechatpay-Signature");
            String serialNo = request.getHeader("Wechatpay-Serial");
            
            // 读取请求体
            String body = getRequestBody(request);
            logger.info("微信退款回调通知: timestamp={}, nonceStr={}, signature={}, serialNo={}, body={}", 
                    timestamp, nonceStr, signature, serialNo, body);
            
            // 验证签名
            boolean isValid = weChatPayService.verifyNotifySignature(timestamp, nonceStr, body, signature);
            
            if (isValid) {
                // 处理退款成功逻辑
                // TODO: 根据业务需求处理退款成功后的逻辑
                logger.info("退款回调验证成功，开始处理业务逻辑");
                
                result.put("code", "SUCCESS");
                result.put("message", "成功");
                return ResponseEntity.ok(result);
            } else {
                logger.warn("退款回调签名验证失败");
                result.put("code", "FAIL");
                result.put("message", "签名验证失败");
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            logger.error("处理退款回调失败", e);
            result.put("code", "FAIL");
            result.put("message", "处理失败");
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 读取请求体内容
     * @param request HTTP请求
     * @return 请求体字符串
     * @throws IOException IO异常
     */
    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
    
    /**
     * 创建支付订单请求模型
     */
    public static class CreatePayOrderRequest {
        private String description;
        private String outTradeNo;
        private Integer totalAmount;
        private String openid;
        private String attach;
        
        // Getters and Setters
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getOutTradeNo() {
            return outTradeNo;
        }
        
        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }
        
        public Integer getTotalAmount() {
            return totalAmount;
        }
        
        public void setTotalAmount(Integer totalAmount) {
            this.totalAmount = totalAmount;
        }
        
        public String getOpenid() {
            return openid;
        }
        
        public void setOpenid(String openid) {
            this.openid = openid;
        }
        
        public String getAttach() {
            return attach;
        }
        
        public void setAttach(String attach) {
            this.attach = attach;
        }
    }
    
    /**
     * 创建退款请求模型
     */
    public static class CreateRefundRequest {
        private String outTradeNo;
        private String outRefundNo;
        private Integer refundAmount;
        private Integer totalAmount;
        private String reason;
        
        // Getters and Setters
        public String getOutTradeNo() {
            return outTradeNo;
        }
        
        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }
        
        public String getOutRefundNo() {
            return outRefundNo;
        }
        
        public void setOutRefundNo(String outRefundNo) {
            this.outRefundNo = outRefundNo;
        }
        
        public Integer getRefundAmount() {
            return refundAmount;
        }
        
        public void setRefundAmount(Integer refundAmount) {
            this.refundAmount = refundAmount;
        }
        
        public Integer getTotalAmount() {
            return totalAmount;
        }
        
        public void setTotalAmount(Integer totalAmount) {
            this.totalAmount = totalAmount;
        }
        
        public String getReason() {
            return reason;
        }
        
        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}