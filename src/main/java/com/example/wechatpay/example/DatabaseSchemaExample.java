package com.example.wechatpay.example;

import com.example.wechatpay.service.DatabaseSchemaService;
import com.example.wechatpay.util.DatabaseSchemaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据库表字段添加示例
 * 演示如何使用DatabaseSchemaService为数据库表添加字段
 */
@Component
public class DatabaseSchemaExample implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSchemaExample.class);

    @Autowired
    private DatabaseSchemaService databaseSchemaService;

    @Autowired
    private DatabaseSchemaUtils databaseSchemaUtils;

    @Override
    public void run(String... args) throws Exception {
        // 注意：这个示例默认不会自动运行，需要手动调用
        // runExamples();
    }

    /**
     * 运行所有示例
     */
    public void runExamples() {
        logger.info("=== 数据库表字段管理示例开始 ===");
        
        try {
            // 示例1：查看所有表
            example1_showAllTables();
            
            // 示例2：为所有表添加单个字段
            example2_addSingleFieldToAllTables();
            
            // 示例3：为指定表添加字段
            example3_addFieldToSpecificTables();
            
            // 示例4：为所有表添加常用字段
            example4_addCommonFields();
            
            // 示例5：检查字段是否存在
            example5_checkFieldExists();
            
        } catch (Exception e) {
            logger.error("示例执行失败: {}", e.getMessage(), e);
        }
        
        logger.info("=== 数据库表字段管理示例结束 ===");
    }

    /**
     * 示例1：查看所有表
     */
    private void example1_showAllTables() {
        logger.info("\n--- 示例1：查看所有表 ---");
        
        try {
            List<String> tableNames = databaseSchemaUtils.getAllTableNames();
            logger.info("数据库中共有 {} 个表:", tableNames.size());
            for (String tableName : tableNames) {
                logger.info("  - {}", tableName);
            }

            // 获取表的详细信息
            List<DatabaseSchemaService.TableInfo> tablesInfo = databaseSchemaService.getAllTablesInfo();
            logger.info("\n表详细信息:");
            for (DatabaseSchemaService.TableInfo tableInfo : tablesInfo) {
                logger.info("  表: {} ({}列)", tableInfo.getTableName(), tableInfo.getColumnCount());
            }
        } catch (Exception e) {
            logger.error("查看表信息失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 示例2：为所有表添加单个字段
     */
    private void example2_addSingleFieldToAllTables() {
        logger.info("\n--- 示例2：为所有表添加单个字段 ---");
        
        try {
            // 为所有表添加一个备注字段
            DatabaseSchemaService.SchemaUpdateResult result = databaseSchemaService.addFieldToAllTables(
                    "remark", 
                    "VARCHAR(500)", 
                    true, 
                    null
            );
            
            logger.info("添加字段结果: {}", result);
            logger.info("成功的表: {}", result.getSuccessTables());
            if (!result.getFailureTables().isEmpty()) {
                logger.warn("失败的表: {}", result.getFailureTables());
            }
        } catch (Exception e) {
            logger.error("为所有表添加字段失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 示例3：为指定表添加字段
     */
    private void example3_addFieldToSpecificTables() {
        logger.info("\n--- 示例3：为指定表添加字段 ---");
        
        try {
            // 假设我们只想为特定的表添加字段
            List<String> targetTables = Arrays.asList("users", "orders", "products");
            
            DatabaseSchemaService.SchemaUpdateResult result = databaseSchemaService.addFieldToSpecificTables(
                    targetTables,
                    "status", 
                    "INT", 
                    false, 
                    "1"
            );
            
            logger.info("为指定表添加字段结果: {}", result);
            logger.info("成功的表: {}", result.getSuccessTables());
            if (!result.getFailureTables().isEmpty()) {
                logger.warn("失败的表: {}", result.getFailureTables());
            }
        } catch (Exception e) {
            logger.error("为指定表添加字段失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 示例4：为所有表添加常用字段
     */
    private void example4_addCommonFields() {
        logger.info("\n--- 示例4：为所有表添加常用字段 ---");
        
        try {
            Map<String, DatabaseSchemaService.SchemaUpdateResult> results = databaseSchemaService.addCommonFieldsToAllTables();
            
            logger.info("批量添加常用字段结果:");
            for (Map.Entry<String, DatabaseSchemaService.SchemaUpdateResult> entry : results.entrySet()) {
                DatabaseSchemaService.SchemaUpdateResult result = entry.getValue();
                logger.info("  字段 {}: 成功 {}/{} 个表", 
                    entry.getKey(), 
                    result.getSuccessCount(), 
                    result.getTotalTables());
            }
        } catch (Exception e) {
            logger.error("批量添加常用字段失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 示例5：检查字段是否存在
     */
    private void example5_checkFieldExists() {
        logger.info("\n--- 示例5：检查字段是否存在 ---");
        
        try {
            List<String> tableNames = databaseSchemaUtils.getAllTableNames();
            if (!tableNames.isEmpty()) {
                String firstTable = tableNames.get(0);
                
                // 检查一些常见字段
                String[] fieldsToCheck = {"id", "created_at", "updated_at", "remark", "status"};
                
                logger.info("检查表 {} 的字段:", firstTable);
                for (String fieldName : fieldsToCheck) {
                    boolean exists = databaseSchemaUtils.columnExists(firstTable, fieldName);
                    logger.info("  字段 {}: {}", fieldName, exists ? "存在" : "不存在");
                }
            }
        } catch (Exception e) {
            logger.error("检查字段是否存在失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 手动执行示例（通过API调用）
     */
    @PostMapping("/run-examples")
    public ResponseEntity<String> runExamplesManually() {
        try {
            runExamples();
            return ResponseEntity.ok("示例执行完成，请查看日志");
        } catch (Exception e) {
            logger.error("手动执行示例失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("示例执行失败: " + e.getMessage());
        }
    }
}