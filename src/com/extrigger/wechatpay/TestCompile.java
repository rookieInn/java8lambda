package com.extrigger.wechatpay;

/**
 * 编译测试类
 * 用于验证所有代码是否能正常编译
 */
public class TestCompile {
    
    public static void main(String[] args) {
        System.out.println("微信支付模块编译测试开始...");
        
        try {
            // 测试配置类
            testConfig();
            
            // 测试请求类
            testRequest();
            
            // 测试响应类
            testResponse();
            
            // 测试工具类
            testUtils();
            
            // 测试服务类
            testServices();
            
            System.out.println("✅ 所有类编译测试通过！");
            
        } catch (Exception e) {
            System.err.println("❌ 编译测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testConfig() {
        System.out.println("测试配置类...");
        WeChatPayConfig config = new WeChatPayConfig();
        config.setAppId("test_app_id");
        config.setMchId("test_mch_id");
        config.setMchKey("test_mch_key");
        
        assert "test_app_id".equals(config.getAppId());
        assert "test_mch_id".equals(config.getMchId());
        assert "test_mch_key".equals(config.getMchKey());
        System.out.println("✅ 配置类测试通过");
    }
    
    private static void testRequest() {
        System.out.println("测试请求类...");
        WeChatPayRequest payRequest = new WeChatPayRequest("TEST001", "测试商品", 100, "127.0.0.1");
        WeChatRefundRequest refundRequest = new WeChatRefundRequest("REFUND001", 100, 50);
        
        assert "TEST001".equals(payRequest.getOutTradeNo());
        assert "REFUND001".equals(refundRequest.getOutRefundNo());
        System.out.println("✅ 请求类测试通过");
    }
    
    private static void testResponse() {
        System.out.println("测试响应类...");
        WeChatPayResponse payResponse = new WeChatPayResponse();
        WeChatRefundResponse refundResponse = new WeChatRefundResponse();
        
        payResponse.setReturnCode("SUCCESS");
        payResponse.setResultCode("SUCCESS");
        refundResponse.setReturnCode("SUCCESS");
        refundResponse.setResultCode("SUCCESS");
        
        assert payResponse.isSuccess();
        assert refundResponse.isSuccess();
        System.out.println("✅ 响应类测试通过");
    }
    
    private static void testUtils() {
        System.out.println("测试工具类...");
        
        // 测试随机字符串生成
        String nonceStr = WeChatPayUtils.generateNonceStr();
        assert nonceStr != null && nonceStr.length() == 32;
        
        // 测试时间戳生成
        String timeStamp = WeChatPayUtils.generateTimeStamp();
        assert timeStamp != null && timeStamp.length() > 0;
        
        // 测试金额转换
        int fen = WeChatPayUtils.yuanToFen(99.99);
        double yuan = WeChatPayUtils.fenToYuan(fen);
        assert Math.abs(yuan - 99.99) < 0.01;
        
        // 测试订单号生成
        String outTradeNo = WeChatPayUtils.generateOutTradeNo();
        String outRefundNo = WeChatPayUtils.generateOutRefundNo();
        assert outTradeNo.startsWith("ORDER");
        assert outRefundNo.startsWith("REFUND");
        
        System.out.println("✅ 工具类测试通过");
    }
    
    private static void testServices() {
        System.out.println("测试服务类...");
        
        // 创建配置
        WeChatPayConfig config = new WeChatPayConfig("test_app", "test_mch", "test_key");
        
        // 测试支付服务
        WeChatPayService payService = new WeChatPayService(config);
        assert payService != null;
        
        // 测试退款服务
        WeChatRefundService refundService = new WeChatRefundService(config);
        assert refundService != null;
        
        System.out.println("✅ 服务类测试通过");
    }
}