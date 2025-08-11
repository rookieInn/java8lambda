import java.util.*;
import java.util.stream.Collectors;

/**
 * 简化的列表工具类，提供常用的列表操作方法
 */
public class ListUtils {
    
    /**
     * 获取两个列表的交集
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 交集列表
     */
    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        if (list1 == null || list2 == null) {
            return new ArrayList<>();
        }
        
        // 使用HashSet提高性能
        Set<T> set = new HashSet<>(list2);
        return list1.stream()
                .filter(set::contains)
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * 获取list1相对于list2的差集（在list1中但不在list2中）
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 差集列表
     */
    public static <T> List<T> difference(List<T> list1, List<T> list2) {
        if (list1 == null) {
            return new ArrayList<>();
        }
        if (list2 == null) {
            return new ArrayList<>(list1);
        }
        
        Set<T> set = new HashSet<>(list2);
        return list1.stream()
                .filter(item -> !set.contains(item))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取两个列表的并集（去重）
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 并集列表
     */
    public static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<>();
        if (list1 != null) {
            set.addAll(list1);
        }
        if (list2 != null) {
            set.addAll(list2);
        }
        return new ArrayList<>(set);
    }
    
    /**
     * 获取对称差集（在list1或list2中，但不同时在两个列表中）
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 对称差集列表
     */
    public static <T> List<T> symmetricDifference(List<T> list1, List<T> list2) {
        List<T> diff1 = difference(list1, list2);
        List<T> diff2 = difference(list2, list1);
        diff1.addAll(diff2);
        return diff1;
    }
    
    /**
     * 判断两个列表是否有交集
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 如果有交集返回true，否则返回false
     */
    public static <T> boolean hasIntersection(List<T> list1, List<T> list2) {
        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) {
            return false;
        }
        
        Set<T> set = new HashSet<>(list2);
        return list1.stream().anyMatch(set::contains);
    }
    
    /**
     * 判断list1是否是list2的子集
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 如果list1是list2的子集返回true，否则返回false
     */
    public static <T> boolean isSubset(List<T> list1, List<T> list2) {
        if (list1 == null || list1.isEmpty()) {
            return true;
        }
        if (list2 == null || list2.isEmpty()) {
            return false;
        }
        
        Set<T> set = new HashSet<>(list2);
        return set.containsAll(list1);
    }
    
    public static void main(String[] args) {
        // 测试示例
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(3, 4, 5, 6, 7);
        
        System.out.println("列表1: " + list1);
        System.out.println("列表2: " + list2);
        System.out.println();
        
        // 交集
        System.out.println("交集: " + intersection(list1, list2));
        // 输出: [3, 4, 5]
        
        // 差集
        System.out.println("差集 (list1 - list2): " + difference(list1, list2));
        // 输出: [1, 2]
        
        System.out.println("差集 (list2 - list1): " + difference(list2, list1));
        // 输出: [6, 7]
        
        // 并集
        System.out.println("并集: " + union(list1, list2));
        // 输出: [1, 2, 3, 4, 5, 6, 7]
        
        // 对称差集
        System.out.println("对称差集: " + symmetricDifference(list1, list2));
        // 输出: [1, 2, 6, 7]
        
        // 判断是否有交集
        System.out.println("是否有交集: " + hasIntersection(list1, list2));
        // 输出: true
        
        // 判断子集
        List<Integer> list3 = Arrays.asList(3, 4);
        System.out.println("list3: " + list3);
        System.out.println("list3是list1的子集: " + isSubset(list3, list1));
        // 输出: true
        
        // 测试空列表
        System.out.println("\n=== 空列表测试 ===");
        List<Integer> emptyList = new ArrayList<>();
        System.out.println("空列表与list1的交集: " + intersection(emptyList, list1));
        System.out.println("list1与null的差集: " + difference(list1, null));
        System.out.println("空列表是list1的子集: " + isSubset(emptyList, list1));
    }
}