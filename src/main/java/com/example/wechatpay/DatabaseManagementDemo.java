package com.example.wechatpay;

import com.example.wechatpay.example.DatabaseManagementExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 数据库管理演示程序
 * 启动后会自动运行数据库字段管理示例
 * 
 * @author Generated
 * @date 2024
 */
@SpringBootApplication
public class DatabaseManagementDemo implements CommandLineRunner {

    @Autowired
    private DatabaseManagementExample databaseManagementExample;

    public static void main(String[] args) {
        SpringApplication.run(DatabaseManagementDemo.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==========================================");
        System.out.println("    数据库字段管理演示程序启动");
        System.out.println("==========================================");
        
        try {
            // 运行所有示例
            databaseManagementExample.runAllExamples();
        } catch (Exception e) {
            System.err.println("运行示例时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n==========================================");
        System.out.println("    演示程序运行完成");
        System.out.println("    可以通过以下API接口使用功能:");
        System.out.println("    GET  /api/database/tables - 获取所有表名");
        System.out.println("    POST /api/database/tables/columns - 为所有表添加字段");
        System.out.println("    详细API文档请查看: DATABASE_MANAGEMENT_API.md");
        System.out.println("==========================================");
    }
}