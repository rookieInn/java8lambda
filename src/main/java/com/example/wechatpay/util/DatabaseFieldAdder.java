package com.example.wechatpay.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 数据库字段添加命令行工具
 * 可以独立运行，为数据库表批量添加字段
 */
@Configuration
@ComponentScan(basePackages = "com.example.wechatpay")
public class DatabaseFieldAdder {

    public static void main(String[] args) {
        System.out.println("=== 数据库表字段添加工具 ===");
        
        try {
            // 初始化Spring上下文
            ApplicationContext context = new AnnotationConfigApplicationContext(DatabaseFieldAdder.class);
            DatabaseSchemaUtils schemaUtils = context.getBean(DatabaseSchemaUtils.class);
            
            Scanner scanner = new Scanner(System.in);
            
            // 显示菜单
            showMenu();
            
            while (true) {
                System.out.print("\n请选择操作 (输入数字): ");
                String choice = scanner.nextLine().trim();
                
                try {
                    switch (choice) {
                        case "1":
                            showAllTables(schemaUtils);
                            break;
                        case "2":
                            addSingleFieldToAllTables(schemaUtils, scanner);
                            break;
                        case "3":
                            addCommonFieldsToAllTables(schemaUtils);
                            break;
                        case "4":
                            addFieldToSpecificTables(schemaUtils, scanner);
                            break;
                        case "5":
                            checkFieldExists(schemaUtils, scanner);
                            break;
                        case "0":
                            System.out.println("退出程序");
                            return;
                        default:
                            System.out.println("无效选择，请重新输入");
                            showMenu();
                    }
                } catch (Exception e) {
                    System.err.println("操作失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
        } catch (Exception e) {
            System.err.println("程序启动失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showMenu() {
        System.out.println("\n可用操作:");
        System.out.println("1. 查看所有表");
        System.out.println("2. 为所有表添加单个字段");
        System.out.println("3. 为所有表添加常用字段 (created_at, updated_at, version, deleted)");
        System.out.println("4. 为指定表添加字段");
        System.out.println("5. 检查字段是否存在");
        System.out.println("0. 退出");
    }

    private static void showAllTables(DatabaseSchemaUtils schemaUtils) {
        System.out.println("\n=== 所有表列表 ===");
        List<String> tables = schemaUtils.getAllTableNames();
        if (tables.isEmpty()) {
            System.out.println("数据库中没有找到表");
        } else {
            System.out.println("共找到 " + tables.size() + " 个表:");
            for (int i = 0; i < tables.size(); i++) {
                System.out.println((i + 1) + ". " + tables.get(i));
            }
        }
    }

    private static void addSingleFieldToAllTables(DatabaseSchemaUtils schemaUtils, Scanner scanner) {
        System.out.println("\n=== 为所有表添加单个字段 ===");
        
        System.out.print("请输入字段名: ");
        String fieldName = scanner.nextLine().trim();
        
        System.out.print("请输入字段类型 (如: VARCHAR(100), INT, TIMESTAMP): ");
        String fieldType = scanner.nextLine().trim();
        
        System.out.print("是否允许为空 (y/n, 默认y): ");
        String nullableInput = scanner.nextLine().trim();
        boolean nullable = nullableInput.isEmpty() || nullableInput.toLowerCase().startsWith("y");
        
        System.out.print("请输入默认值 (可为空): ");
        String defaultValue = scanner.nextLine().trim();
        if (defaultValue.isEmpty()) {
            defaultValue = null;
        }
        
        System.out.println("\n确认信息:");
        System.out.println("字段名: " + fieldName);
        System.out.println("字段类型: " + fieldType);
        System.out.println("允许为空: " + (nullable ? "是" : "否"));
        System.out.println("默认值: " + (defaultValue == null ? "无" : defaultValue));
        
        System.out.print("\n确认执行? (y/n): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.toLowerCase().startsWith("y")) {
            List<String> tables = schemaUtils.getAllTableNames();
            System.out.println("\n开始为 " + tables.size() + " 个表添加字段...");
            
            int successCount = 0;
            int failureCount = 0;
            
            for (String tableName : tables) {
                try {
                    schemaUtils.addColumnToTable(tableName, fieldName, fieldType, nullable, defaultValue);
                    System.out.println("✓ " + tableName + " - 成功");
                    successCount++;
                } catch (Exception e) {
                    System.out.println("✗ " + tableName + " - 失败: " + e.getMessage());
                    failureCount++;
                }
            }
            
            System.out.println("\n执行结果: 成功 " + successCount + " 个，失败 " + failureCount + " 个");
        } else {
            System.out.println("操作已取消");
        }
    }

    private static void addCommonFieldsToAllTables(DatabaseSchemaUtils schemaUtils) {
        System.out.println("\n=== 为所有表添加常用字段 ===");
        System.out.println("将添加以下字段:");
        System.out.println("- created_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)");
        System.out.println("- updated_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
        System.out.println("- version (INT, NOT NULL, DEFAULT 0)");
        System.out.println("- deleted (TINYINT(1), NOT NULL, DEFAULT 0)");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n确认执行? (y/n): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.toLowerCase().startsWith("y")) {
            List<String> tables = schemaUtils.getAllTableNames();
            System.out.println("\n开始为 " + tables.size() + " 个表添加常用字段...");
            
            // 添加created_at字段
            addFieldToAllTablesHelper(schemaUtils, "created_at", "TIMESTAMP", false, "CURRENT_TIMESTAMP");
            
            // 添加updated_at字段
            String databaseType = schemaUtils.getDatabaseType();
            String updatedAtDefault = databaseType.contains("mysql") ? 
                "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" : "CURRENT_TIMESTAMP";
            addFieldToAllTablesHelper(schemaUtils, "updated_at", "TIMESTAMP", false, updatedAtDefault);
            
            // 添加version字段
            addFieldToAllTablesHelper(schemaUtils, "version", "INT", false, "0");
            
            // 添加deleted字段
            addFieldToAllTablesHelper(schemaUtils, "deleted", "TINYINT(1)", false, "0");
            
            System.out.println("\n常用字段添加完成!");
        } else {
            System.out.println("操作已取消");
        }
    }

    private static void addFieldToAllTablesHelper(DatabaseSchemaUtils schemaUtils, String fieldName, String fieldType, boolean nullable, String defaultValue) {
        List<String> tables = schemaUtils.getAllTableNames();
        int successCount = 0;
        int failureCount = 0;
        
        System.out.println("\n添加字段: " + fieldName);
        for (String tableName : tables) {
            try {
                schemaUtils.addColumnToTable(tableName, fieldName, fieldType, nullable, defaultValue);
                System.out.println("  ✓ " + tableName);
                successCount++;
            } catch (Exception e) {
                System.out.println("  ✗ " + tableName + " - " + e.getMessage());
                failureCount++;
            }
        }
        System.out.println("字段 " + fieldName + " 添加结果: 成功 " + successCount + " 个，失败 " + failureCount + " 个");
    }

    private static void addFieldToSpecificTables(DatabaseSchemaUtils schemaUtils, Scanner scanner) {
        System.out.println("\n=== 为指定表添加字段 ===");
        
        // 显示所有表供选择
        List<String> allTables = schemaUtils.getAllTableNames();
        System.out.println("可用的表:");
        for (int i = 0; i < allTables.size(); i++) {
            System.out.println((i + 1) + ". " + allTables.get(i));
        }
        
        System.out.print("\n请输入要操作的表名 (用逗号分隔): ");
        String tableInput = scanner.nextLine().trim();
        List<String> targetTables = Arrays.asList(tableInput.split(","));
        
        // 清理表名
        targetTables.replaceAll(String::trim);
        
        System.out.print("请输入字段名: ");
        String fieldName = scanner.nextLine().trim();
        
        System.out.print("请输入字段类型: ");
        String fieldType = scanner.nextLine().trim();
        
        System.out.print("是否允许为空 (y/n, 默认y): ");
        String nullableInput = scanner.nextLine().trim();
        boolean nullable = nullableInput.isEmpty() || nullableInput.toLowerCase().startsWith("y");
        
        System.out.print("请输入默认值 (可为空): ");
        String defaultValue = scanner.nextLine().trim();
        if (defaultValue.isEmpty()) {
            defaultValue = null;
        }
        
        System.out.println("\n将为以下表添加字段:");
        targetTables.forEach(table -> System.out.println("  - " + table));
        
        System.out.print("\n确认执行? (y/n): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.toLowerCase().startsWith("y")) {
            int successCount = 0;
            int failureCount = 0;
            
            for (String tableName : targetTables) {
                try {
                    schemaUtils.addColumnToTable(tableName, fieldName, fieldType, nullable, defaultValue);
                    System.out.println("✓ " + tableName + " - 成功");
                    successCount++;
                } catch (Exception e) {
                    System.out.println("✗ " + tableName + " - 失败: " + e.getMessage());
                    failureCount++;
                }
            }
            
            System.out.println("\n执行结果: 成功 " + successCount + " 个，失败 " + failureCount + " 个");
        } else {
            System.out.println("操作已取消");
        }
    }

    private static void checkFieldExists(DatabaseSchemaUtils schemaUtils, Scanner scanner) {
        System.out.println("\n=== 检查字段是否存在 ===");
        
        System.out.print("请输入表名: ");
        String tableName = scanner.nextLine().trim();
        
        System.out.print("请输入字段名: ");
        String fieldName = scanner.nextLine().trim();
        
        try {
            boolean exists = schemaUtils.columnExists(tableName, fieldName);
            System.out.println("\n结果: 表 " + tableName + " 中字段 " + fieldName + " " + (exists ? "存在" : "不存在"));
            
            if (exists) {
                // 显示字段详细信息
                List<DatabaseSchemaUtils.ColumnInfo> columns = schemaUtils.getTableColumns(tableName);
                for (DatabaseSchemaUtils.ColumnInfo column : columns) {
                    if (column.getColumnName().equals(fieldName)) {
                        System.out.println("字段详细信息: " + column);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("检查失败: " + e.getMessage());
        }
    }
}