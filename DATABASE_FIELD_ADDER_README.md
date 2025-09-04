# 数据库字段批量添加工具

这是一个用Java编写的工具，可以批量为数据库中的所有表添加指定字段。支持MySQL、PostgreSQL、Oracle、SQL Server和H2等主流数据库。

## 功能特性

1. **多数据库支持**：支持MySQL、PostgreSQL、Oracle、SQL Server、H2等数据库
2. **事务管理**：支持事务模式，确保操作的原子性
3. **错误处理**：完善的错误处理和日志记录
4. **字段检查**：自动检查字段是否已存在，避免重复添加
5. **灵活配置**：支持通过配置文件设置数据库连接和字段信息
6. **图形界面**：提供GUI版本，方便非技术人员使用
7. **执行报告**：自动生成执行报告，记录成功和失败的表

## 项目结构

```
src/main/java/com/example/database/
├── DatabaseFieldAdder.java          # 基础版本
├── EnhancedDatabaseFieldAdder.java  # 增强版本（支持事务）
└── DatabaseFieldAdderGUI.java       # 图形界面版本

src/main/resources/
├── database.properties                    # 主配置文件模板
├── database-example-update-time.properties # 示例：添加更新时间
├── database-example-status.properties      # 示例：添加状态字段
└── database-example-deleted.properties     # 示例：添加逻辑删除字段
```

## 使用方法

### 1. 命令行方式

#### 编译项目
```bash
mvn clean compile
```

#### 列出所有表
```bash
java -cp target/classes:lib/* com.example.database.EnhancedDatabaseFieldAdder src/main/resources/database.properties list
```

#### 添加字段到所有表
```bash
java -cp target/classes:lib/* com.example.database.EnhancedDatabaseFieldAdder src/main/resources/database.properties add
```

#### 验证字段是否添加成功
```bash
java -cp target/classes:lib/* com.example.database.EnhancedDatabaseFieldAdder src/main/resources/database.properties verify created_time
```

### 2. 图形界面方式

```bash
java -cp target/classes:lib/* com.example.database.DatabaseFieldAdderGUI
```

### 3. 配置文件说明

配置文件采用标准的properties格式：

```properties
# 数据库连接配置
database.url=jdbc:mysql://localhost:3306/your_database?useSSL=false
database.driver=com.mysql.cj.jdbc.Driver
database.username=root
database.password=your_password
database.schema=  # 可选，某些数据库需要

# 字段配置
field.name=created_time       # 字段名称
field.type=DATETIME          # 字段类型
field.default=CURRENT_TIMESTAMP  # 默认值
field.notNull=false          # 是否非空
field.comment=创建时间        # 字段注释

# 其他配置
use.transaction=true         # 是否使用事务
```

## 字段类型对照表

### MySQL
- 时间类型：DATETIME, TIMESTAMP, DATE, TIME
- 字符串类型：VARCHAR(n), CHAR(n), TEXT, LONGTEXT
- 数值类型：INT, BIGINT, DECIMAL(p,s), FLOAT, DOUBLE
- 布尔类型：BOOLEAN, TINYINT(1)

### PostgreSQL
- 时间类型：TIMESTAMP, DATE, TIME
- 字符串类型：VARCHAR(n), CHAR(n), TEXT
- 数值类型：INTEGER, BIGINT, NUMERIC(p,s), REAL, DOUBLE PRECISION
- 布尔类型：BOOLEAN

### Oracle
- 时间类型：DATE, TIMESTAMP
- 字符串类型：VARCHAR2(n), CHAR(n), CLOB
- 数值类型：NUMBER, NUMBER(p,s), FLOAT
- 布尔类型：NUMBER(1)

### SQL Server
- 时间类型：DATETIME, DATE, TIME
- 字符串类型：VARCHAR(n), CHAR(n), TEXT, NVARCHAR(n)
- 数值类型：INT, BIGINT, DECIMAL(p,s), FLOAT, REAL
- 布尔类型：BIT

## 常用字段添加示例

### 1. 添加创建时间字段
```properties
field.name=created_time
field.type=DATETIME
field.default=CURRENT_TIMESTAMP
field.notNull=false
field.comment=记录创建时间
```

### 2. 添加更新时间字段
```properties
field.name=updated_time
field.type=DATETIME
field.default=CURRENT_TIMESTAMP
field.notNull=false
field.comment=记录更新时间
```

### 3. 添加状态字段
```properties
field.name=status
field.type=INT
field.default=1
field.notNull=true
field.comment=状态：1-启用，0-禁用
```

### 4. 添加逻辑删除字段
```properties
field.name=is_deleted
field.type=TINYINT(1)
field.default=0
field.notNull=true
field.comment=逻辑删除标记：0-未删除，1-已删除
```

### 5. 添加创建人字段
```properties
field.name=created_by
field.type=VARCHAR(50)
field.default=
field.notNull=false
field.comment=创建人
```

## 注意事项

1. **备份数据**：在执行操作前，请务必备份数据库
2. **测试环境**：建议先在测试环境中验证
3. **权限检查**：确保数据库用户有ALTER TABLE权限
4. **事务模式**：默认开启事务模式，任何一个表添加失败都会回滚
5. **字段命名**：不同数据库对字段名的要求不同，请遵循相应规范
6. **默认值**：某些数据库不支持某些默认值表达式
7. **系统表**：工具会自动过滤系统表，只处理用户表

## 错误处理

1. **字段已存在**：自动跳过，不会报错
2. **表不存在**：记录错误，继续处理其他表
3. **权限不足**：记录错误信息
4. **连接失败**：检查数据库配置和网络

## 执行报告

每次执行完成后，会在当前目录生成执行报告文件：
- 文件名格式：`field_addition_report_时间戳.txt`
- 内容包括：成功的表列表、失败的表列表及错误原因

## 扩展开发

如需自定义功能，可以：
1. 继承`EnhancedDatabaseFieldAdder`类
2. 重写相关方法
3. 添加新的字段处理逻辑

## 常见问题

### Q: 为什么某些表添加字段失败？
A: 可能原因包括：
- 字段已存在
- 表被锁定
- 权限不足
- 字段类型不支持

### Q: 如何处理大量表的情况？
A: 建议：
- 分批处理
- 使用事务模式确保一致性
- 监控数据库负载

### Q: 是否支持添加索引？
A: 当前版本只支持添加字段，不支持索引。可以在添加字段后手动创建索引。

## 许可证

本项目采用 MIT 许可证。