# 数据库表字段管理工具

这个工具提供了使用Java为数据库的各张表批量添加字段的功能。

## 功能特性

- ✅ 支持MySQL和PostgreSQL数据库
- ✅ 批量为所有表添加字段
- ✅ 为指定表添加字段
- ✅ 添加常用字段（创建时间、更新时间、版本号、软删除标记）
- ✅ 检查字段是否已存在，避免重复添加
- ✅ 提供REST API和命令行工具两种使用方式
- ✅ 完整的日志记录和错误处理

## 配置说明

### 1. 数据库配置

在 `src/main/resources/application.yml` 中配置数据库连接信息：

```yaml
spring:
  datasource:
    # MySQL配置
    url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
    
    # 或者 PostgreSQL配置
    # url: jdbc:postgresql://localhost:5432/your_database
    # username: your_username
    # password: your_password
    # driver-class-name: org.postgresql.Driver
```

### 2. Maven依赖

项目已包含必要的依赖：
- MySQL/PostgreSQL JDBC驱动
- HikariCP连接池
- Spring JDBC

## 使用方式

### 方式1：REST API

启动Spring Boot应用后，可以通过以下API端点操作：

#### 查看所有表
```
GET /api/database/schema/tables
```

#### 为所有表添加字段
```
POST /api/database/schema/add-field/all
Content-Type: application/json

{
    "fieldName": "remark",
    "fieldType": "VARCHAR(500)",
    "nullable": true,
    "defaultValue": null
}
```

#### 为指定表添加字段
```
POST /api/database/schema/add-field/specific
Content-Type: application/json

{
    "tableNames": ["users", "orders", "products"],
    "fieldName": "status",
    "fieldType": "INT",
    "nullable": false,
    "defaultValue": "1"
}
```

#### 添加常用字段
```
POST /api/database/schema/add-common-fields
```

### 方式2：命令行工具

运行命令行工具：

```bash
java -cp target/classes:target/lib/* com.example.wechatpay.util.DatabaseFieldAdder
```

工具提供交互式菜单，可以：
1. 查看所有表
2. 为所有表添加单个字段
3. 为所有表添加常用字段
4. 为指定表添加字段
5. 检查字段是否存在

### 方式3：编程方式

```java
@Autowired
private DatabaseSchemaService databaseSchemaService;

// 为所有表添加字段
SchemaUpdateResult result = databaseSchemaService.addFieldToAllTables(
    "remark", "VARCHAR(500)", true, null
);

// 为所有表添加常用字段
Map<String, SchemaUpdateResult> results = databaseSchemaService.addCommonFieldsToAllTables();

// 为指定表添加字段
List<String> tables = Arrays.asList("users", "orders");
SchemaUpdateResult result = databaseSchemaService.addFieldToSpecificTables(
    tables, "status", "INT", false, "1"
);
```

## 常用字段说明

工具预定义了一些常用字段：

| 字段名 | 类型 | 说明 | 默认值 |
|--------|------|------|--------|
| created_at | TIMESTAMP | 创建时间 | CURRENT_TIMESTAMP |
| updated_at | TIMESTAMP | 更新时间 | CURRENT_TIMESTAMP (MySQL支持ON UPDATE) |
| version | INT | 版本号(乐观锁) | 0 |
| deleted | TINYINT(1) | 软删除标记 | 0 |

## 支持的数据类型

### MySQL
- `VARCHAR(n)` - 变长字符串
- `CHAR(n)` - 定长字符串
- `TEXT` - 长文本
- `INT` - 整数
- `BIGINT` - 长整数
- `DECIMAL(p,s)` - 精确数值
- `TIMESTAMP` - 时间戳
- `DATETIME` - 日期时间
- `DATE` - 日期
- `TINYINT(1)` - 布尔值

### PostgreSQL
- `VARCHAR(n)` - 变长字符串
- `CHAR(n)` - 定长字符串
- `TEXT` - 长文本
- `INTEGER` - 整数
- `BIGINT` - 长整数
- `NUMERIC(p,s)` - 精确数值
- `TIMESTAMP` - 时间戳
- `DATE` - 日期
- `BOOLEAN` - 布尔值

## 安全注意事项

1. **备份数据库**: 在执行任何表结构修改前，请务必备份数据库
2. **测试环境**: 建议先在测试环境验证
3. **权限检查**: 确保数据库用户有ALTER TABLE权限
4. **字段命名**: 遵循数据库命名规范
5. **数据类型**: 根据实际需求选择合适的数据类型

## 错误处理

- 工具会自动检查字段是否已存在，避免重复添加
- 每个表的操作都是独立的，单个表失败不会影响其他表
- 提供详细的日志记录，便于问题排查
- 操作结果包含成功和失败的表列表

## 示例场景

### 场景1：为所有表添加审计字段
```java
// 添加创建时间和更新时间
databaseSchemaService.addCreatedAtToAllTables();
databaseSchemaService.addUpdatedAtToAllTables();
```

### 场景2：为用户相关表添加状态字段
```java
List<String> userTables = Arrays.asList("users", "user_profiles", "user_settings");
databaseSchemaService.addFieldToSpecificTables(userTables, "status", "INT", false, "1");
```

### 场景3：为所有表添加软删除支持
```java
databaseSchemaService.addSoftDeleteToAllTables();
```

## 注意事项

1. 确保数据库连接配置正确
2. 执行前请备份重要数据
3. 大型数据库操作可能需要较长时间
4. 某些字段类型在不同数据库中可能有差异
5. 建议在维护窗口期间执行批量操作