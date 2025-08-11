# Java List比较操作指南

这个项目展示了如何在Java中比较两个List，获取它们的交集、差集、并集等操作。

## 文件说明

- `ListComparison.java` - 完整的示例程序，展示多种List比较方法
- `ListUtils.java` - 实用的工具类，提供简洁的List比较方法
- `ListUtilsExample.java` - 使用工具类的示例程序
- `README.md` - 本说明文件

## 主要功能

### 1. 交集 (Intersection)
获取两个List中都包含的元素。

```java
// 使用Stream API
List<String> intersection = list1.stream()
    .filter(list2::contains)
    .collect(Collectors.toList());

// 使用工具类
List<String> intersection = ListUtils.getIntersection(list1, list2);

// 使用Collection方法
List<String> intersection = new ArrayList<>(list1);
intersection.retainAll(list2);
```

### 2. 差集 (Difference)
获取在一个List中存在但在另一个List中不存在的元素。

```java
// 获取list1相对于list2的差集
List<String> difference = ListUtils.getDifference(list1, list2);

// 使用Collection方法
List<String> difference = new ArrayList<>(list1);
difference.removeAll(list2);
```

### 3. 并集 (Union)
获取两个List中所有不重复的元素。

```java
List<String> union = ListUtils.getUnion(list1, list2);
```

### 4. 对称差集 (Symmetric Difference)
获取两个List的并集减去交集。

```java
List<String> symmetricDiff = ListUtils.getSymmetricDifference(list1, list2);
```

## 性能比较

对于大数据集，不同方法的性能排序（从快到慢）：

1. **Set方法** - 最快，时间复杂度O(n)
2. **Collection方法** - 中等，时间复杂度O(n²)
3. **Stream API** - 最慢，时间复杂度O(n²)

## 使用建议

### 小数据集 (< 1000元素)
- 推荐使用Stream API，代码简洁易读

### 大数据集 (≥ 1000元素)
- 推荐使用Set方法，性能最佳
- 使用`getIntersectionSet()`和`getDifferenceSet()`方法

### 需要保持顺序
- 使用`LinkedHashSet`来保持插入顺序
- 使用`ArrayList`的`retainAll()`和`removeAll()`方法

## 注意事项

1. **对象比较**：如果List中包含自定义对象，需要重写`equals()`和`hashCode()`方法
2. **空值处理**：工具类已经处理了null值情况，返回空集合
3. **重复元素**：Set方法会自动去重，Stream API会保留重复元素
4. **顺序**：Set方法不保证元素顺序，Stream API保持原List的顺序

## 运行示例

```bash
# 编译所有Java文件
javac *.java

# 运行完整示例
java ListComparison

# 运行工具类示例
java ListUtilsExample
```

## 输出示例

```
=== 字符串List比较 ===
List1: [apple, banana, orange, grape]
List2: [banana, grape, mango, pear]
交集: [banana, grape]
List1 - List2: [apple, orange]
List2 - List1: [mango, pear]
并集: [apple, banana, orange, grape, mango, pear]
对称差集: [apple, orange, mango, pear]
```

## 扩展功能

工具类还提供了：
- 相等性检查（考虑/忽略顺序）
- 空值安全处理
- 泛型支持
- 详细的JavaDoc注释

这些方法可以满足大多数List比较的需求，同时保持良好的性能和代码可读性。
