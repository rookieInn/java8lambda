package com.example.wechatpay.controller;

import com.example.wechatpay.service.DatabaseSchemaService;
import com.example.wechatpay.util.DatabaseSchemaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据库表结构管理控制器
 * 提供REST API来管理数据库表字段
 */
@RestController
@RequestMapping("/api/database/schema")
public class DatabaseSchemaController {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSchemaController.class);

    @Autowired
    private DatabaseSchemaService databaseSchemaService;

    @Autowired
    private DatabaseSchemaUtils databaseSchemaUtils;

    /**
     * 获取所有表名
     */
    @GetMapping("/tables")
    public ResponseEntity<List<String>> getAllTables() {
        try {
            List<String> tables = databaseSchemaUtils.getAllTableNames();
            return ResponseEntity.ok(tables);
        } catch (Exception e) {
            logger.error("获取表名失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取所有表的详细信息
     */
    @GetMapping("/tables/info")
    public ResponseEntity<List<DatabaseSchemaService.TableInfo>> getAllTablesInfo() {
        try {
            List<DatabaseSchemaService.TableInfo> tablesInfo = databaseSchemaService.getAllTablesInfo();
            return ResponseEntity.ok(tablesInfo);
        } catch (Exception e) {
            logger.error("获取表信息失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 为所有表添加字段
     */
    @PostMapping("/add-field/all")
    public ResponseEntity<DatabaseSchemaService.SchemaUpdateResult> addFieldToAllTables(@RequestBody AddFieldRequest request) {
        try {
            logger.info("收到为所有表添加字段的请求: {}", request);
            DatabaseSchemaService.SchemaUpdateResult result = databaseSchemaService.addFieldToAllTables(
                    request.getFieldName(),
                    request.getFieldType(),
                    request.isNullable(),
                    request.getDefaultValue()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("为所有表添加字段失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 为指定表添加字段
     */
    @PostMapping("/add-field/specific")
    public ResponseEntity<DatabaseSchemaService.SchemaUpdateResult> addFieldToSpecificTables(@RequestBody AddFieldToTablesRequest request) {
        try {
            logger.info("收到为指定表添加字段的请求: {}", request);
            DatabaseSchemaService.SchemaUpdateResult result = databaseSchemaService.addFieldToSpecificTables(
                    request.getTableNames(),
                    request.getFieldName(),
                    request.getFieldType(),
                    request.isNullable(),
                    request.getDefaultValue()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("为指定表添加字段失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 为所有表添加常用字段（created_at, updated_at, version, deleted）
     */
    @PostMapping("/add-common-fields")
    public ResponseEntity<Map<String, DatabaseSchemaService.SchemaUpdateResult>> addCommonFieldsToAllTables() {
        try {
            logger.info("收到为所有表添加常用字段的请求");
            Map<String, DatabaseSchemaService.SchemaUpdateResult> results = databaseSchemaService.addCommonFieldsToAllTables();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("为所有表添加常用字段失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 为所有表添加创建时间字段
     */
    @PostMapping("/add-created-at")
    public ResponseEntity<DatabaseSchemaService.SchemaUpdateResult> addCreatedAtToAllTables() {
        try {
            DatabaseSchemaService.SchemaUpdateResult result = databaseSchemaService.addCreatedAtToAllTables();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("为所有表添加创建时间字段失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 为所有表添加更新时间字段
     */
    @PostMapping("/add-updated-at")
    public ResponseEntity<DatabaseSchemaService.SchemaUpdateResult> addUpdatedAtToAllTables() {
        try {
            DatabaseSchemaService.SchemaUpdateResult result = databaseSchemaService.addUpdatedAtToAllTables();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("为所有表添加更新时间字段失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 检查字段是否存在
     */
    @GetMapping("/check-field/{tableName}/{fieldName}")
    public ResponseEntity<Boolean> checkFieldExists(@PathVariable String tableName, @PathVariable String fieldName) {
        try {
            boolean exists = databaseSchemaUtils.columnExists(tableName, fieldName);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            logger.error("检查字段是否存在失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 添加字段请求类
     */
    public static class AddFieldRequest {
        private String fieldName;
        private String fieldType;
        private boolean nullable = true;
        private String defaultValue;

        // Constructors
        public AddFieldRequest() {}

        public AddFieldRequest(String fieldName, String fieldType, boolean nullable, String defaultValue) {
            this.fieldName = fieldName;
            this.fieldType = fieldType;
            this.nullable = nullable;
            this.defaultValue = defaultValue;
        }

        // Getters and Setters
        public String getFieldName() { return fieldName; }
        public void setFieldName(String fieldName) { this.fieldName = fieldName; }
        public String getFieldType() { return fieldType; }
        public void setFieldType(String fieldType) { this.fieldType = fieldType; }
        public boolean isNullable() { return nullable; }
        public void setNullable(boolean nullable) { this.nullable = nullable; }
        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

        @Override
        public String toString() {
            return String.format("AddFieldRequest{fieldName='%s', fieldType='%s', nullable=%s, defaultValue='%s'}",
                    fieldName, fieldType, nullable, defaultValue);
        }
    }

    /**
     * 为指定表添加字段请求类
     */
    public static class AddFieldToTablesRequest extends AddFieldRequest {
        private List<String> tableNames;

        // Constructors
        public AddFieldToTablesRequest() {}

        public AddFieldToTablesRequest(List<String> tableNames, String fieldName, String fieldType, boolean nullable, String defaultValue) {
            super(fieldName, fieldType, nullable, defaultValue);
            this.tableNames = tableNames;
        }

        // Getters and Setters
        public List<String> getTableNames() { return tableNames; }
        public void setTableNames(List<String> tableNames) { this.tableNames = tableNames; }

        @Override
        public String toString() {
            return String.format("AddFieldToTablesRequest{tableNames=%s, fieldName='%s', fieldType='%s', nullable=%s, defaultValue='%s'}",
                    tableNames, getFieldName(), getFieldType(), isNullable(), getDefaultValue());
        }
    }
}