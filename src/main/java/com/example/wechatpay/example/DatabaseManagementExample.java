package com.example.wechatpay.example;

import com.example.wechatpay.service.ColumnManagementService;
import com.example.wechatpay.service.TableManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理使用示例
 * 演示如何使用数据库字段管理功能
 * 
 * @author Generated
 * @date 2024
 */
@Component
public class DatabaseManagementExample {

    @Autowired
    private TableManagementService tableManagementService;

    @Autowired
    private ColumnManagementService columnManagementService;

    /**
     * 示例：为所有表添加创建时间字段
     */
    public void addCreateTimeToAllTables() {
        System.out.println("=== 为所有表添加创建时间字段 ===");
        
        List<Map<String, Object>> results = columnManagementService.addColumnToAllTables(
                "create_time",           // 字段名
                "DATETIME",             // 字段类型
                "CURRENT_TIMESTAMP",    // 默认值
                false,                  // 不允许为空
                "创建时间"              // 字段注释
        );
        
        // 打印结果
        for (Map<String, Object> result : results) {
            System.out.println("表: " + result.get("tableName") + 
                             ", 成功: " + result.get("success") + 
                             ", 消息: " + result.get("message"));
        }
    }

    /**
     * 示例：为所有表添加更新时间字段
     */
    public void addUpdateTimeToAllTables() {
        System.out.println("=== 为所有表添加更新时间字段 ===");
        
        List<Map<String, Object>> results = columnManagementService.addColumnToAllTables(
                "update_time",          // 字段名
                "DATETIME",             // 字段类型
                "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", // 默认值
                true,                   // 允许为空
                "更新时间"              // 字段注释
        );
        
        // 打印结果
        for (Map<String, Object> result : results) {
            System.out.println("表: " + result.get("tableName") + 
                             ", 成功: " + result.get("success") + 
                             ", 消息: " + result.get("message"));
        }
    }

    /**
     * 示例：为所有表添加删除标记字段
     */
    public void addDeleteFlagToAllTables() {
        System.out.println("=== 为所有表添加删除标记字段 ===");
        
        List<Map<String, Object>> results = columnManagementService.addColumnToAllTables(
                "is_deleted",           // 字段名
                "TINYINT(1)",           // 字段类型
                "0",                    // 默认值
                false,                  // 不允许为空
                "删除标记(0:未删除,1:已删除)" // 字段注释
        );
        
        // 打印结果
        for (Map<String, Object> result : results) {
            System.out.println("表: " + result.get("tableName") + 
                             ", 成功: " + result.get("success") + 
                             ", 消息: " + result.get("message"));
        }
    }

    /**
     * 示例：为指定表添加字段
     */
    public void addColumnToSpecificTables() {
        System.out.println("=== 为指定表添加字段 ===");
        
        List<String> tableNames = Arrays.asList("user", "order", "product");
        
        List<Map<String, Object>> results = columnManagementService.addColumnToSpecificTables(
                tableNames,
                "version",              // 字段名
                "INT",                  // 字段类型
                "1",                    // 默认值
                false,                  // 不允许为空
                "版本号(用于乐观锁)"     // 字段注释
        );
        
        // 打印结果
        for (Map<String, Object> result : results) {
            System.out.println("表: " + result.get("tableName") + 
                             ", 成功: " + result.get("success") + 
                             ", 消息: " + result.get("message"));
        }
    }

    /**
     * 示例：查看数据库中的所有表
     */
    public void showAllTables() {
        System.out.println("=== 数据库中的所有表 ===");
        
        List<String> tableNames = tableManagementService.getAllTableNames();
        System.out.println("表数量: " + tableNames.size());
        
        for (String tableName : tableNames) {
            System.out.println("- " + tableName);
        }
    }

    /**
     * 示例：查看指定表的字段信息
     */
    public void showTableColumns(String tableName) {
        System.out.println("=== 表 " + tableName + " 的字段信息 ===");
        
        if (!tableManagementService.tableExists(tableName)) {
            System.out.println("表 " + tableName + " 不存在");
            return;
        }
        
        List<Map<String, Object>> columns = tableManagementService.getTableColumns(tableName);
        System.out.println("字段数量: " + columns.size());
        
        for (Map<String, Object> column : columns) {
            System.out.println("- " + column.get("Field") + " " + 
                             column.get("Type") + " " + 
                             (column.get("Null").equals("YES") ? "NULL" : "NOT NULL") + 
                             (column.get("Default") != null ? " DEFAULT " + column.get("Default") : ""));
        }
    }

    /**
     * 运行所有示例
     */
    public void runAllExamples() {
        System.out.println("开始运行数据库管理示例...\n");
        
        // 查看所有表
        showAllTables();
        System.out.println();
        
        // 为所有表添加常用字段
        addCreateTimeToAllTables();
        System.out.println();
        
        addUpdateTimeToAllTables();
        System.out.println();
        
        addDeleteFlagToAllTables();
        System.out.println();
        
        // 为指定表添加字段
        addColumnToSpecificTables();
        System.out.println();
        
        System.out.println("数据库管理示例运行完成！");
    }
}