# 数据库字段管理 API 使用说明

## 概述

本系统提供了完整的数据库表字段管理功能，可以通过 REST API 为数据库中的表添加字段。支持为单个表、多个指定表或所有表批量添加字段。

## 配置说明

### 1. 数据库配置

在 `application.yml` 中配置数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
```

### 2. 依赖说明

项目已包含以下数据库相关依赖：
- MySQL 驱动
- Spring Boot JDBC
- HikariCP 连接池
- MyBatis

## API 接口说明

### 1. 获取所有表名

**接口地址：** `GET /api/database/tables`

**响应示例：**
```json
{
  "success": true,
  "data": ["user", "order", "product"],
  "count": 3,
  "message": "获取表列表成功"
}
```

### 2. 获取指定表的字段信息

**接口地址：** `GET /api/database/tables/{tableName}/columns`

**响应示例：**
```json
{
  "success": true,
  "data": [
    {
      "Field": "id",
      "Type": "int(11)",
      "Null": "NO",
      "Key": "PRI",
      "Default": null,
      "Extra": "auto_increment"
    }
  ],
  "tableName": "user",
  "count": 1,
  "message": "获取表字段信息成功"
}
```

### 3. 为单个表添加字段

**接口地址：** `POST /api/database/tables/{tableName}/columns`

**请求体：**
```json
{
  "columnName": "create_time",
  "columnType": "DATETIME",
  "defaultValue": "CURRENT_TIMESTAMP",
  "nullable": false,
  "comment": "创建时间"
}
```

**响应示例：**
```json
{
  "success": true,
  "message": "成功为表 user 添加字段 create_time",
  "sql": "ALTER TABLE user ADD COLUMN create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'"
}
```

### 4. 为所有表添加字段

**接口地址：** `POST /api/database/tables/columns`

**请求体：**
```json
{
  "columnName": "is_deleted",
  "columnType": "TINYINT(1)",
  "defaultValue": "0",
  "nullable": false,
  "comment": "删除标记(0:未删除,1:已删除)"
}
```

**响应示例：**
```json
{
  "success": true,
  "data": [
    {
      "success": true,
      "message": "成功为表 user 添加字段 is_deleted",
      "tableName": "user"
    },
    {
      "success": true,
      "message": "成功为表 order 添加字段 is_deleted",
      "tableName": "order"
    }
  ],
  "totalTables": 2,
  "successCount": 2,
  "failCount": 0,
  "message": "批量添加字段完成，成功: 2, 失败: 0"
}
```

### 5. 为指定表列表添加字段

**接口地址：** `POST /api/database/tables/columns/batch`

**请求体：**
```json
{
  "tableNames": ["user", "order", "product"],
  "columnName": "version",
  "columnType": "INT",
  "defaultValue": "1",
  "nullable": false,
  "comment": "版本号(用于乐观锁)"
}
```

### 6. 获取常用字段类型

**接口地址：** `GET /api/database/column-types`

**响应示例：**
```json
{
  "success": true,
  "data": [
    "VARCHAR(255)",
    "TEXT",
    "INT",
    "BIGINT",
    "DECIMAL(10,2)",
    "DATETIME",
    "DATE",
    "TIME",
    "TIMESTAMP",
    "BOOLEAN",
    "TINYINT(1)",
    "JSON"
  ],
  "message": "获取字段类型列表成功"
}
```

## 常用字段类型说明

| 类型 | 说明 | 示例 |
|------|------|------|
| VARCHAR(n) | 可变长度字符串 | VARCHAR(255) |
| TEXT | 长文本 | TEXT |
| INT | 整数 | INT |
| BIGINT | 长整数 | BIGINT |
| DECIMAL(p,s) | 精确小数 | DECIMAL(10,2) |
| DATETIME | 日期时间 | DATETIME |
| DATE | 日期 | DATE |
| TIME | 时间 | TIME |
| TIMESTAMP | 时间戳 | TIMESTAMP |
| BOOLEAN | 布尔值 | BOOLEAN |
| TINYINT(1) | 小整数(常用于布尔) | TINYINT(1) |
| JSON | JSON数据 | JSON |

## 使用示例

### 1. 为所有表添加审计字段

```bash
# 添加创建时间字段
curl -X POST http://localhost:8080/api/database/tables/columns \
  -H "Content-Type: application/json" \
  -d '{
    "columnName": "create_time",
    "columnType": "DATETIME",
    "defaultValue": "CURRENT_TIMESTAMP",
    "nullable": false,
    "comment": "创建时间"
  }'

# 添加更新时间字段
curl -X POST http://localhost:8080/api/database/tables/columns \
  -H "Content-Type: application/json" \
  -d '{
    "columnName": "update_time",
    "columnType": "DATETIME",
    "defaultValue": "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
    "nullable": true,
    "comment": "更新时间"
  }'

# 添加删除标记字段
curl -X POST http://localhost:8080/api/database/tables/columns \
  -H "Content-Type: application/json" \
  -d '{
    "columnName": "is_deleted",
    "columnType": "TINYINT(1)",
    "defaultValue": "0",
    "nullable": false,
    "comment": "删除标记(0:未删除,1:已删除)"
  }'
```

### 2. 为指定表添加版本字段

```bash
curl -X POST http://localhost:8080/api/database/tables/columns/batch \
  -H "Content-Type: application/json" \
  -d '{
    "tableNames": ["user", "order", "product"],
    "columnName": "version",
    "columnType": "INT",
    "defaultValue": "1",
    "nullable": false,
    "comment": "版本号(用于乐观锁)"
  }'
```

## 注意事项

1. **备份数据**：在执行字段添加操作前，建议先备份数据库
2. **权限检查**：确保数据库用户具有 ALTER TABLE 权限
3. **字段重复**：系统会自动检查字段是否已存在，避免重复添加
4. **事务支持**：所有操作都在事务中执行，确保数据一致性
5. **错误处理**：API 会返回详细的操作结果和错误信息

## 错误处理

当操作失败时，API 会返回如下格式的错误信息：

```json
{
  "success": false,
  "message": "具体错误信息",
  "error": "异常类型"
}
```

常见错误：
- 表不存在
- 字段已存在
- 数据库连接失败
- SQL 语法错误
- 权限不足

## 扩展功能

如需扩展功能，可以：

1. 在 `ColumnManagementService` 中添加新的字段操作方法
2. 在 `DatabaseManagementController` 中添加新的 API 接口
3. 支持更多数据库类型（PostgreSQL、Oracle 等）
4. 添加字段删除、修改功能
5. 添加索引管理功能