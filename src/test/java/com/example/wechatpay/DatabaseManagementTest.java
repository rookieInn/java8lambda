package com.example.wechatpay;

import com.example.wechatpay.service.ColumnManagementService;
import com.example.wechatpay.service.TableManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理功能测试类
 * 
 * @author Generated
 * @date 2024
 */
@SpringBootTest
public class DatabaseManagementTest {

    @Autowired
    private TableManagementService tableManagementService;

    @Autowired
    private ColumnManagementService columnManagementService;

    /**
     * 测试获取所有表名
     */
    @Test
    public void testGetAllTableNames() {
        System.out.println("=== 测试获取所有表名 ===");
        
        List<String> tableNames = tableManagementService.getAllTableNames();
        System.out.println("数据库中的表数量: " + tableNames.size());
        
        for (String tableName : tableNames) {
            System.out.println("- " + tableName);
        }
    }

    /**
     * 测试为所有表添加创建时间字段
     */
    @Test
    public void testAddCreateTimeToAllTables() {
        System.out.println("=== 测试为所有表添加创建时间字段 ===");
        
        List<Map<String, Object>> results = columnManagementService.addColumnToAllTables(
                "create_time",
                "DATETIME",
                "CURRENT_TIMESTAMP",
                false,
                "创建时间"
        );
        
        System.out.println("操作结果:");
        for (Map<String, Object> result : results) {
            System.out.println("表: " + result.get("tableName") + 
                             ", 成功: " + result.get("success") + 
                             ", 消息: " + result.get("message"));
        }
    }

    /**
     * 测试为指定表添加字段
     */
    @Test
    public void testAddColumnToSpecificTables() {
        System.out.println("=== 测试为指定表添加字段 ===");
        
        List<String> tableNames = Arrays.asList("user", "order", "product");
        
        List<Map<String, Object>> results = columnManagementService.addColumnToSpecificTables(
                tableNames,
                "version",
                "INT",
                "1",
                false,
                "版本号"
        );
        
        System.out.println("操作结果:");
        for (Map<String, Object> result : results) {
            System.out.println("表: " + result.get("tableName") + 
                             ", 成功: " + result.get("success") + 
                             ", 消息: " + result.get("message"));
        }
    }

    /**
     * 测试获取常用字段类型
     */
    @Test
    public void testGetCommonColumnTypes() {
        System.out.println("=== 测试获取常用字段类型 ===");
        
        List<String> types = columnManagementService.getCommonColumnTypes();
        System.out.println("常用字段类型:");
        
        for (String type : types) {
            System.out.println("- " + type);
        }
    }

    /**
     * 测试检查表是否存在
     */
    @Test
    public void testTableExists() {
        System.out.println("=== 测试检查表是否存在 ===");
        
        // 获取所有表名
        List<String> tableNames = tableManagementService.getAllTableNames();
        
        if (!tableNames.isEmpty()) {
            String firstTable = tableNames.get(0);
            boolean exists = tableManagementService.tableExists(firstTable);
            System.out.println("表 " + firstTable + " 是否存在: " + exists);
        }
        
        // 测试不存在的表
        boolean notExists = tableManagementService.tableExists("non_existent_table");
        System.out.println("不存在的表检查结果: " + notExists);
    }

    /**
     * 测试检查字段是否存在
     */
    @Test
    public void testColumnExists() {
        System.out.println("=== 测试检查字段是否存在 ===");
        
        // 获取所有表名
        List<String> tableNames = tableManagementService.getAllTableNames();
        
        if (!tableNames.isEmpty()) {
            String firstTable = tableNames.get(0);
            
            // 检查常见字段
            boolean idExists = tableManagementService.columnExists(firstTable, "id");
            System.out.println("表 " + firstTable + " 中 id 字段是否存在: " + idExists);
            
            boolean createTimeExists = tableManagementService.columnExists(firstTable, "create_time");
            System.out.println("表 " + firstTable + " 中 create_time 字段是否存在: " + createTimeExists);
        }
    }
}