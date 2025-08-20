package com.extrigger.wechatpay;

/**
 * 微信支付使用示例
 * 展示如何使用支付和退款服务
 */
public class WeChatPayExample {
    
    public static void main(String[] args) {
        try {
            // 1. 配置微信支付参数
            WeChatPayConfig config = new WeChatPayConfig();
            config.setAppId("your_app_id");
            config.setMchId("your_mch_id");
            config.setMchKey("your_mch_key");
            config.setNotifyUrl("https://your.domain.com/pay/notify");
            config.setRefundNotifyUrl("https://your.domain.com/refund/notify");
            
            // 2. 创建支付服务
            WeChatPayService payService = new WeChatPayService(config);
            WeChatRefundService refundService = new WeChatRefundService(config);
            
            // 3. 统一下单示例
            unifiedOrderExample(payService);
            
            // 4. 查询订单示例
            queryOrderExample(payService);
            
            // 5. 申请退款示例
            refundExample(refundService);
            
            // 6. 查询退款示例
            queryRefundExample(refundService);
            
        } catch (Exception e) {
            System.err.println("微信支付示例执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 统一下单示例
     */
    private static void unifiedOrderExample(WeChatPayService payService) throws Exception {
        System.out.println("=== 统一下单示例 ===");
        
        // 创建支付请求
        WeChatPayRequest request = new WeChatPayRequest();
        request.setOutTradeNo(WeChatPayUtils.generateOutTradeNo());
        request.setBody("测试商品");
        request.setTotalFee(WeChatPayUtils.yuanToFen(0.01)); // 1分钱
        request.setSpbillCreateIp("127.0.0.1");
        request.setTradeType("NATIVE"); // 扫码支付
        
        // 调用统一下单
        WeChatPayResponse response = payService.unifiedOrder(request);
        
        if (response.isSuccess()) {
            System.out.println("统一下单成功！");
            System.out.println("预支付ID: " + response.getPrepayId());
            System.out.println("二维码链接: " + response.getCodeUrl());
            System.out.println("商户订单号: " + request.getOutTradeNo());
        } else {
            System.out.println("统一下单失败！");
            System.out.println("错误代码: " + response.getErrCode());
            System.out.println("错误描述: " + response.getErrCodeDes());
        }
        System.out.println();
    }
    
    /**
     * 查询订单示例
     */
    private static void queryOrderExample(WeChatPayService payService) throws Exception {
        System.out.println("=== 查询订单示例 ===");
        
        // 这里使用一个示例订单号，实际使用时应该是真实的订单号
        String outTradeNo = "ORDER" + System.currentTimeMillis();
        
        try {
            WeChatPayResponse response = payService.queryOrder(null, outTradeNo);
            
            if (response.isReturnSuccess()) {
                if (response.isResultSuccess()) {
                    System.out.println("查询订单成功！");
                    System.out.println("订单状态: " + response.getResultCode());
                } else {
                    System.out.println("查询订单业务失败！");
                    System.out.println("错误代码: " + response.getErrCode());
                    System.out.println("错误描述: " + response.getErrCodeDes());
                }
            } else {
                System.out.println("查询订单通信失败！");
                System.out.println("返回信息: " + response.getReturnMsg());
            }
        } catch (Exception e) {
            System.out.println("查询订单异常: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * 申请退款示例
     */
    private static void refundExample(WeChatRefundService refundService) throws Exception {
        System.out.println("=== 申请退款示例 ===");
        
        // 这里使用示例数据，实际使用时应该是真实的订单何以
        String outTradeNo = "ORDER" + System.currentTimeMillis();
        String transactionId = "wx" + System.currentTimeMillis();
        int totalFee = WeChatPayUtils.yuanToFen(0.01); // 1分钱
        int refundFee = WeChatPayUtils.yuanToFen(0.01); // 全额退款
        String refundDesc = "用户申请退款";
        
        try {
            WeChatRefundResponse response = refundService.refund(outTradeNo, transactionId, totalFee, refundFee, refundDesc);
            
            if (response.isSuccess()) {
                System.out.println("申请退款成功！");
                System.out.println("微信退款单号: " + response.getRefundId());
                System.out.println("商户退款单号: " + response.getOutRefundNo());
                System.out.println("退款金额: " + WeChatPayUtils.fenToYuan(response.getRefundFee()) + "元");
            } else {
                System.out.println("申请退款失败！");
                System.out.println("错误代码: " + response.getErrCode());
                System.out.println("错误描述: " + response.getErrCodeDes());
            }
        } catch (Exception e) {
            System.out.println("申请退款异常: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * 查询退款示例
     */
    private static void queryRefundExample(WeChatRefundService refundService) throws Exception {
        System.out.println("=== 查询退款示例 ===");
        
        // 这里使用示例数据，实际使用时应该是真实的订单号
        String outTradeNo = "ORDER" + System.currentTimeMillis();
        
        try {
            WeChatRefundResponse response = refundService.queryRefund(null, outTradeNo, null, null);
            
            if (response.isReturnSuccess()) {
                if (response.isResultSuccess()) {
                    System.out.println("查询退款成功！");
                    if (response.getRefundId() != null) {
                        System.out.println("微信退款单号: " + response.getRefundId());
                        System.out.println("退款状态: 退款成功");
                    } else {
                        System.out.println("退款状态: 退款处理中");
                    }
                } else {
                    System.out.println("查询退款业务失败！");
                    System.out.println("错误代码: " + response.getErrCode());
                    System.out.println("错误描述: " + response.getErrCodeDes());
                }
            } else {
                System.out.println("查询退款通信失败！");
                System.out.println("返回信息: " + response.getReturnMsg());
            }
        } catch (Exception e) {
            System.out.println("查询退款异常: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * 工具方法示例
     */
    private static void utilsExample() {
        System.out.println("=== 工具方法示例 ===");
        
        // 生成随机字符串
        String nonceStr = WeChatPayUtils.generateNonceStr();
        System.out.println("随机字符串: " + nonceStr);
        
        // 生成时间戳
        String timeStamp = WeChatPayUtils.generateTimeStamp();
        System.out.println("时间戳: " + timeStamp);
        
        // 金额转换
        double yuan = 99.99;
        int fen = WeChatPayUtils.yuanToFen(yuan);
        System.out.println(yuan + "元 = " + fen + "分");
        
        double yuanBack = WeChatPayUtils.fenToYuan(fen);
        System.out.println(fen + "分 = " + yuanBack + "元");
        
        // 格式化金额
        String formattedAmount = WeChatPayUtils.formatAmount(yuan);
        System.out.println("格式化金额: " + formattedAmount);
        
        // 生成订单号
        String outTradeNo = WeChatPayUtils.generateOutTradeNo();
        System.out.println("商户订单号: " + outTradeNo);
        
        // 生成退款单号
        String outRefundNo = WeChatPayUtils.generateOutRefundNo();
        System.out.println("商户退款单号: " + outRefundNo);
        
        System.out.println();
    }
}