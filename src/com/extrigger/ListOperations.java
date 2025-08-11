package com.extrigger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 演示如何计算两个List的交集和差集
 */
public class ListOperations {
    
    /**
     * 计算两个List的交集
     * 方法1：使用retainAll()
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 交集结果
     */
    public static <T> List<T> getIntersection(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>(list1);
        result.retainAll(list2);
        return result;
    }
    
    /**
     * 计算两个List的交集
     * 方法2：使用Stream API
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 交集结果
     */
    public static <T> List<T> getIntersectionStream(List<T> list1, List<T> list2) {
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }
    
    /**
     * 计算两个List的差集 (list1 - list2)
     * 方法1：使用removeAll()
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 差集结果
     */
    public static <T> List<T> getDifference(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>(list1);
        result.removeAll(list2);
        return result;
    }
    
    /**
     * 计算两个List的差集 (list1 - list2)
     * 方法2：使用Stream API
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 差集结果
     */
    public static <T> List<T> getDifferenceStream(List<T> list1, List<T> list2) {
        return list1.stream()
                .filter(element -> !list2.contains(element))
                .collect(Collectors.toList());
    }
    
    /**
     * 计算两个List的对称差集 (list1 ⊕ list2)
     * 即：(list1 - list2) ∪ (list2 - list1)
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 对称差集结果
     */
    public static <T> List<T> getSymmetricDifference(List<T> list1, List<T> list2) {
        List<T> diff1 = getDifference(list1, list2);
        List<T> diff2 = getDifference(list2, list1);
        
        List<T> result = new ArrayList<>(diff1);
        result.addAll(diff2);
        return result;
    }
    
    /**
     * 计算两个List的并集
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @return 并集结果（去重）
     */
    public static <T> List<T> getUnion(List<T> list1, List<T> list2) {
        Set<T> unionSet = new HashSet<>(list1);
        unionSet.addAll(list2);
        return new ArrayList<>(unionSet);
    }
    
    /**
     * 主方法：演示各种集合操作
     */
    public static void main(String[] args) {
        // 创建两个示例List
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> list2 = Arrays.asList(4, 5, 6, 7, 8, 9);
        
        System.out.println("List1: " + list1);
        System.out.println("List2: " + list2);
        System.out.println();
        
        // 计算交集
        List<Integer> intersection1 = getIntersection(list1, list2);
        List<Integer> intersection2 = getIntersectionStream(list1, list2);
        System.out.println("交集 (retainAll方法): " + intersection1);
        System.out.println("交集 (Stream方法): " + intersection2);
        System.out.println();
        
        // 计算差集
        List<Integer> difference1 = getDifference(list1, list2);
        List<Integer> difference2 = getDifferenceStream(list1, list2);
        System.out.println("差集 list1 - list2 (removeAll方法): " + difference1);
        System.out.println("差集 list1 - list2 (Stream方法): " + difference2);
        System.out.println();
        
        List<Integer> difference3 = getDifference(list2, list1);
        System.out.println("差集 list2 - list1: " + difference3);
        System.out.println();
        
        // 计算对称差集
        List<Integer> symmetricDiff = getSymmetricDifference(list1, list2);
        System.out.println("对称差集: " + symmetricDiff);
        System.out.println();
        
        // 计算并集
        List<Integer> union = getUnion(list1, list2);
        System.out.println("并集: " + union);
        System.out.println();
        
        // 字符串示例
        System.out.println("========== 字符串示例 ==========");
        List<String> fruits1 = Arrays.asList("苹果", "香蕉", "橘子", "葡萄");
        List<String> fruits2 = Arrays.asList("香蕉", "葡萄", "西瓜", "草莓");
        
        System.out.println("水果列表1: " + fruits1);
        System.out.println("水果列表2: " + fruits2);
        System.out.println();
        
        System.out.println("交集: " + getIntersection(fruits1, fruits2));
        System.out.println("差集 (列表1 - 列表2): " + getDifference(fruits1, fruits2));
        System.out.println("差集 (列表2 - 列表1): " + getDifference(fruits2, fruits1));
        System.out.println("对称差集: " + getSymmetricDifference(fruits1, fruits2));
        System.out.println("并集: " + getUnion(fruits1, fruits2));
    }
}