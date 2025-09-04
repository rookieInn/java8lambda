package com.example.wechatpay.test;

import com.example.wechatpay.service.DatabaseSchemaService;
import com.example.wechatpay.util.DatabaseSchemaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据库表结构管理测试类
 * 用于测试数据库字段添加功能
 */
@Component
public class DatabaseSchemaTest {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSchemaTest.class);

    @Autowired
    private DatabaseSchemaService databaseSchemaService;

    @Autowired
    private DatabaseSchemaUtils databaseSchemaUtils;

    /**
     * 测试基本功能
     */
    public void testBasicFunctions() {
        logger.info("=== 开始测试数据库表结构管理功能 ===");
        
        try {
            // 测试1：获取所有表
            testGetAllTables();
            
            // 测试2：检查字段是否存在
            testCheckFieldExists();
            
            // 测试3：获取表列信息
            testGetTableColumns();
            
            logger.info("=== 基本功能测试完成 ===");
        } catch (Exception e) {
            logger.error("基本功能测试失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 测试字段添加功能（谨慎使用，会修改数据库结构）
     */
    public void testAddFields() {
        logger.info("=== 开始测试字段添加功能 ===");
        logger.warn("注意：此测试会修改数据库结构，请确保在测试环境中运行！");
        
        try {
            // 测试1：为所有表添加测试字段
            testAddFieldToAllTables();
            
            // 测试2：为指定表添加字段
            testAddFieldToSpecificTables();
            
            logger.info("=== 字段添加功能测试完成 ===");
        } catch (Exception e) {
            logger.error("字段添加功能测试失败: {}", e.getMessage(), e);
        }
    }

    private void testGetAllTables() {
        logger.info("--- 测试获取所有表 ---");
        try {
            List<String> tables = databaseSchemaUtils.getAllTableNames();
            logger.info("数据库中共有 {} 个表", tables.size());
            
            if (tables.size() > 0 && tables.size() <= 10) {
                logger.info("表列表: {}", tables);
            } else if (tables.size() > 10) {
                logger.info("前10个表: {}", tables.subList(0, 10));
                logger.info("... 还有 {} 个表", tables.size() - 10);
            }
        } catch (Exception e) {
            logger.error("获取表列表测试失败: {}", e.getMessage(), e);
        }
    }

    private void testCheckFieldExists() {
        logger.info("--- 测试检查字段是否存在 ---");
        try {
            List<String> tables = databaseSchemaUtils.getAllTableNames();
            if (!tables.isEmpty()) {
                String testTable = tables.get(0);
                
                // 测试一些常见字段
                String[] commonFields = {"id", "name", "created_at", "updated_at", "status"};
                
                logger.info("检查表 {} 的字段存在性:", testTable);
                for (String field : commonFields) {
                    boolean exists = databaseSchemaUtils.columnExists(testTable, field);
                    logger.info("  字段 {}: {}", field, exists ? "存在" : "不存在");
                }
            }
        } catch (Exception e) {
            logger.error("检查字段存在性测试失败: {}", e.getMessage(), e);
        }
    }

    private void testGetTableColumns() {
        logger.info("--- 测试获取表列信息 ---");
        try {
            List<String> tables = databaseSchemaUtils.getAllTableNames();
            if (!tables.isEmpty()) {
                String testTable = tables.get(0);
                
                List<DatabaseSchemaUtils.ColumnInfo> columns = databaseSchemaUtils.getTableColumns(testTable);
                logger.info("表 {} 的列信息 (共{}列):", testTable, columns.size());
                
                for (DatabaseSchemaUtils.ColumnInfo column : columns) {
                    logger.info("  {}: {} (nullable={}, default={})", 
                        column.getColumnName(), 
                        column.getDataType(),
                        column.isNullable(),
                        column.getDefaultValue());
                }
            }
        } catch (Exception e) {
            logger.error("获取表列信息测试失败: {}", e.getMessage(), e);
        }
    }

    private void testAddFieldToAllTables() {
        logger.info("--- 测试为所有表添加字段 ---");
        logger.warn("这将修改数据库结构！");
        
        try {
            // 添加一个测试字段
            DatabaseSchemaService.SchemaUpdateResult result = databaseSchemaService.addFieldToAllTables(
                    "test_field", 
                    "VARCHAR(100)", 
                    true, 
                    "'test_default'"
            );
            
            logger.info("添加字段结果: {}", result);
            logger.info("成功: {} 个表", result.getSuccessCount());
            logger.info("失败: {} 个表", result.getFailureCount());
            
            if (!result.getFailureTables().isEmpty()) {
                logger.warn("失败的表: {}", result.getFailureTables());
            }
            
        } catch (Exception e) {
            logger.error("为所有表添加字段测试失败: {}", e.getMessage(), e);
        }
    }

    private void testAddFieldToSpecificTables() {
        logger.info("--- 测试为指定表添加字段 ---");
        
        try {
            List<String> allTables = databaseSchemaUtils.getAllTableNames();
            if (allTables.size() >= 2) {
                // 选择前两个表进行测试
                List<String> testTables = allTables.subList(0, Math.min(2, allTables.size()));
                
                DatabaseSchemaService.SchemaUpdateResult result = databaseSchemaService.addFieldToSpecificTables(
                        testTables,
                        "specific_test_field", 
                        "INT", 
                        false, 
                        "0"
                );
                
                logger.info("为指定表添加字段结果: {}", result);
                logger.info("目标表: {}", testTables);
                logger.info("成功: {} 个表", result.getSuccessCount());
                logger.info("失败: {} 个表", result.getFailureCount());
            } else {
                logger.warn("数据库中表数量不足，跳过指定表测试");
            }
            
        } catch (Exception e) {
            logger.error("为指定表添加字段测试失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 运行所有测试（仅查询，不修改数据库）
     */
    public void runSafeTests() {
        logger.info("运行安全测试（不修改数据库结构）");
        testBasicFunctions();
    }

    /**
     * 运行完整测试（包括修改数据库结构的测试）
     * 警告：这会修改数据库结构！
     */
    public void runFullTests() {
        logger.warn("运行完整测试（会修改数据库结构）");
        testBasicFunctions();
        testAddFields();
    }
}