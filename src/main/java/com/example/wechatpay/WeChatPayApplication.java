package com.example.wechatpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 微信支付应用主类
 */
@SpringBootApplication
@EnableConfigurationProperties
public class WeChatPayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WeChatPayApplication.class, args);
        System.out.println("========================================");
        System.out.println("    微信支付服务启动成功！");
        System.out.println("    端口: 8080");
        System.out.println("========================================");
    }
}