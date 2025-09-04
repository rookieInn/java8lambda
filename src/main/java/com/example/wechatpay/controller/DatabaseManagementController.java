package com.example.wechatpay.controller;

import com.example.wechatpay.service.ColumnManagementService;
import com.example.wechatpay.service.TableManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理控制器
 * 提供数据库表字段管理的REST API接口
 * 
 * @author Generated
 * @date 2024
 */
@RestController
@RequestMapping("/api/database")
@CrossOrigin(origins = "*")
public class DatabaseManagementController {

    @Autowired
    private TableManagementService tableManagementService;

    @Autowired
    private ColumnManagementService columnManagementService;

    /**
     * 获取所有表名
     * 
     * @return 表名列表
     */
    @GetMapping("/tables")
    public ResponseEntity<Map<String, Object>> getAllTables() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<String> tableNames = tableManagementService.getAllTableNames();
            response.put("success", true);
            response.put("data", tableNames);
            response.put("count", tableNames.size());
            response.put("message", "获取表列表成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取表列表失败: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取指定表的字段信息
     * 
     * @param tableName 表名
     * @return 字段信息
     */
    @GetMapping("/tables/{tableName}/columns")
    public ResponseEntity<Map<String, Object>> getTableColumns(@PathVariable String tableName) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (!tableManagementService.tableExists(tableName)) {
                response.put("success", false);
                response.put("message", "表 " + tableName + " 不存在");
                return ResponseEntity.ok(response);
            }
            
            List<Map<String, Object>> columns = tableManagementService.getTableColumns(tableName);
            response.put("success", true);
            response.put("data", columns);
            response.put("tableName", tableName);
            response.put("count", columns.size());
            response.put("message", "获取表字段信息成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取表字段信息失败: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 为单个表添加字段
     * 
     * @param request 添加字段请求
     * @return 操作结果
     */
    @PostMapping("/tables/{tableName}/columns")
    public ResponseEntity<Map<String, Object>> addColumnToTable(
            @PathVariable String tableName,
            @RequestBody AddColumnRequest request) {
        
        Map<String, Object> result = columnManagementService.addColumnToTable(
                tableName,
                request.getColumnName(),
                request.getColumnType(),
                request.getDefaultValue(),
                request.isNullable(),
                request.getComment()
        );
        
        return ResponseEntity.ok(result);
    }

    /**
     * 为所有表添加字段
     * 
     * @param request 添加字段请求
     * @return 操作结果列表
     */
    @PostMapping("/tables/columns")
    public ResponseEntity<Map<String, Object>> addColumnToAllTables(
            @RequestBody AddColumnRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> results = columnManagementService.addColumnToAllTables(
                    request.getColumnName(),
                    request.getColumnType(),
                    request.getDefaultValue(),
                    request.isNullable(),
                    request.getComment()
            );
            
            // 统计成功和失败的数量
            long successCount = results.stream().mapToLong(r -> (Boolean) r.get("success") ? 1 : 0).sum();
            long failCount = results.size() - successCount;
            
            response.put("success", true);
            response.put("data", results);
            response.put("totalTables", results.size());
            response.put("successCount", successCount);
            response.put("failCount", failCount);
            response.put("message", String.format("批量添加字段完成，成功: %d, 失败: %d", successCount, failCount));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量添加字段失败: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 为指定表列表添加字段
     * 
     * @param request 批量添加字段请求
     * @return 操作结果列表
     */
    @PostMapping("/tables/columns/batch")
    public ResponseEntity<Map<String, Object>> addColumnToSpecificTables(
            @RequestBody BatchAddColumnRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Map<String, Object>> results = columnManagementService.addColumnToSpecificTables(
                    request.getTableNames(),
                    request.getColumnName(),
                    request.getColumnType(),
                    request.getDefaultValue(),
                    request.isNullable(),
                    request.getComment()
            );
            
            // 统计成功和失败的数量
            long successCount = results.stream().mapToLong(r -> (Boolean) r.get("success") ? 1 : 0).sum();
            long failCount = results.size() - successCount;
            
            response.put("success", true);
            response.put("data", results);
            response.put("totalTables", results.size());
            response.put("successCount", successCount);
            response.put("failCount", failCount);
            response.put("message", String.format("批量添加字段完成，成功: %d, 失败: %d", successCount, failCount));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量添加字段失败: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取常用字段类型
     * 
     * @return 字段类型列表
     */
    @GetMapping("/column-types")
    public ResponseEntity<Map<String, Object>> getCommonColumnTypes() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<String> types = columnManagementService.getCommonColumnTypes();
            response.put("success", true);
            response.put("data", types);
            response.put("message", "获取字段类型列表成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取字段类型列表失败: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 添加字段请求类
     */
    public static class AddColumnRequest {
        private String columnName;
        private String columnType;
        private String defaultValue;
        private boolean nullable = true;
        private String comment;

        // Getters and Setters
        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnType() {
            return columnType;
        }

        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public boolean isNullable() {
            return nullable;
        }

        public void setNullable(boolean nullable) {
            this.nullable = nullable;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    /**
     * 批量添加字段请求类
     */
    public static class BatchAddColumnRequest extends AddColumnRequest {
        private List<String> tableNames;

        public List<String> getTableNames() {
            return tableNames;
        }

        public void setTableNames(List<String> tableNames) {
            this.tableNames = tableNames;
        }
    }
}