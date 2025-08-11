import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import java.util.*;

/**
 * 使用Google Guava库进行列表操作
 * 需要添加依赖：
 * Maven:
 * <dependency>
 *     <groupId>com.google.guava</groupId>
 *     <artifactId>guava</artifactId>
 *     <version>31.1-jre</version>
 * </dependency>
 * 
 * Gradle:
 * implementation 'com.google.guava:guava:31.1-jre'
 */
public class ListOperationsWithGuava {
    
    /**
     * 使用Guava获取交集
     */
    public static <T> Set<T> getIntersectionGuava(Collection<T> list1, Collection<T> list2) {
        return Sets.intersection(new HashSet<>(list1), new HashSet<>(list2));
    }
    
    /**
     * 使用Guava获取差集
     */
    public static <T> Set<T> getDifferenceGuava(Collection<T> list1, Collection<T> list2) {
        return Sets.difference(new HashSet<>(list1), new HashSet<>(list2));
    }
    
    /**
     * 使用Guava获取对称差集
     */
    public static <T> Set<T> getSymmetricDifferenceGuava(Collection<T> list1, Collection<T> list2) {
        return Sets.symmetricDifference(new HashSet<>(list1), new HashSet<>(list2));
    }
    
    /**
     * 使用Guava获取并集
     */
    public static <T> Set<T> getUnionGuava(Collection<T> list1, Collection<T> list2) {
        return Sets.union(new HashSet<>(list1), new HashSet<>(list2));
    }
    
    /**
     * 性能优化版本：使用HashSet进行大数据量的交集操作
     */
    public static <T> List<T> getIntersectionOptimized(List<T> list1, List<T> list2) {
        // 将较小的列表转换为HashSet，提高查找效率
        Set<T> smallerSet;
        List<T> largerList;
        
        if (list1.size() < list2.size()) {
            smallerSet = new HashSet<>(list1);
            largerList = list2;
        } else {
            smallerSet = new HashSet<>(list2);
            largerList = list1;
        }
        
        return largerList.stream()
                .filter(smallerSet::contains)
                .distinct()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * 性能优化版本：使用HashSet进行大数据量的差集操作
     */
    public static <T> List<T> getDifferenceOptimized(List<T> list1, List<T> list2) {
        Set<T> set2 = new HashSet<>(list2);
        return list1.stream()
                .filter(item -> !set2.contains(item))
                .distinct()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public static void main(String[] args) {
        // 示例：使用Guava进行操作
        System.out.println("=== 使用Guava库的示例 ===");
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(3, 4, 5, 6, 7);
        
        System.out.println("列表1: " + list1);
        System.out.println("列表2: " + list2);
        
        // 注意：如果没有Guava依赖，下面的代码会编译错误
        // 请先添加Guava依赖
        /*
        Set<Integer> intersection = getIntersectionGuava(list1, list2);
        System.out.println("交集 (Guava): " + intersection);
        
        Set<Integer> difference = getDifferenceGuava(list1, list2);
        System.out.println("差集 (Guava): " + difference);
        
        Set<Integer> symmetricDiff = getSymmetricDifferenceGuava(list1, list2);
        System.out.println("对称差集 (Guava): " + symmetricDiff);
        
        Set<Integer> union = getUnionGuava(list1, list2);
        System.out.println("并集 (Guava): " + union);
        */
        
        // 使用优化版本
        System.out.println("\n=== 性能优化版本 ===");
        List<Integer> intersectionOpt = getIntersectionOptimized(list1, list2);
        System.out.println("交集 (优化版): " + intersectionOpt);
        
        List<Integer> differenceOpt = getDifferenceOptimized(list1, list2);
        System.out.println("差集 (优化版): " + differenceOpt);
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        performanceTest();
    }
    
    /**
     * 性能测试：比较不同方法的执行时间
     */
    public static void performanceTest() {
        // 创建大列表
        List<Integer> bigList1 = new ArrayList<>();
        List<Integer> bigList2 = new ArrayList<>();
        
        for (int i = 0; i < 10000; i++) {
            bigList1.add(i);
            bigList2.add(i + 5000);
        }
        
        // 测试交集操作
        long startTime = System.nanoTime();
        List<Integer> result1 = new ArrayList<>(bigList1);
        result1.retainAll(bigList2);
        long endTime = System.nanoTime();
        System.out.println("retainAll方法耗时: " + (endTime - startTime) / 1000000.0 + " ms");
        
        startTime = System.nanoTime();
        List<Integer> result2 = getIntersectionOptimized(bigList1, bigList2);
        endTime = System.nanoTime();
        System.out.println("优化版本耗时: " + (endTime - startTime) / 1000000.0 + " ms");
        
        System.out.println("交集元素个数: " + result2.size());
    }
}