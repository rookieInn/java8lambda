import java.util.*;
import java.util.stream.Collectors;

public class ListComparison {
    public static void main(String[] args) {
        // 创建两个示例List
        List<String> list1 = Arrays.asList("apple", "banana", "orange", "grape", "kiwi");
        List<String> list2 = Arrays.asList("banana", "grape", "mango", "pear");
        
        System.out.println("List1: " + list1);
        System.out.println("List2: " + list2);
        System.out.println();
        
        // 方法1: 使用Stream API (推荐)
        System.out.println("=== 使用Stream API ===");
        
        // 交集 (两个List都包含的元素)
        List<String> intersection = list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
        System.out.println("交集: " + intersection);
        
        // 差集 (在list1中但不在list2中的元素)
        List<String> difference1 = list1.stream()
                .filter(item -> !list2.contains(item))
                .collect(Collectors.toList());
        System.out.println("List1 - List2 (差集): " + difference1);
        
        // 差集 (在list2中但不在list1中的元素)
        List<String> difference2 = list2.stream()
                .filter(item -> !list1.contains(item))
                .collect(Collectors.toList());
        System.out.println("List2 - List1 (差集): " + difference2);
        
        // 对称差集 (两个List的并集减去交集)
        List<String> symmetricDifference = new ArrayList<>();
        symmetricDifference.addAll(difference1);
        symmetricDifference.addAll(difference2);
        System.out.println("对称差集: " + symmetricDifference);
        
        System.out.println();
        
        // 方法2: 使用Collection方法
        System.out.println("=== 使用Collection方法 ===");
        
        // 交集
        List<String> intersection2 = new ArrayList<>(list1);
        intersection2.retainAll(list2);
        System.out.println("交集: " + intersection2);
        
        // 差集
        List<String> difference1_2 = new ArrayList<>(list1);
        difference1_2.removeAll(list2);
        System.out.println("List1 - List2 (差集): " + difference1_2);
        
        List<String> difference2_2 = new ArrayList<>(list2);
        difference2_2.removeAll(list1);
        System.out.println("List2 - List1 (差集): " + difference2_2);
        
        System.out.println();
        
        // 方法3: 使用Set (适用于需要去重的场景)
        System.out.println("=== 使用Set方法 ===");
        
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);
        
        // 交集
        Set<String> intersectionSet = new HashSet<>(set1);
        intersectionSet.retainAll(set2);
        System.out.println("交集: " + intersectionSet);
        
        // 差集
        Set<String> differenceSet1 = new HashSet<>(set1);
        differenceSet1.removeAll(set2);
        System.out.println("List1 - List2 (差集): " + differenceSet1);
        
        Set<String> differenceSet2 = new HashSet<>(set2);
        differenceSet2.removeAll(set1);
        System.out.println("List2 - List1 (差集): " + differenceSet2);
        
        // 并集
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("并集: " + union);
        
        System.out.println();
        
        // 性能比较示例
        System.out.println("=== 性能比较 ===");
        List<Integer> largeList1 = new ArrayList<>();
        List<Integer> largeList2 = new ArrayList<>();
        
        // 创建大数据集
        for (int i = 0; i < 10000; i++) {
            largeList1.add(i);
            if (i % 2 == 0) {
                largeList2.add(i);
            }
        }
        
        long startTime = System.currentTimeMillis();
        List<Integer> intersectionLarge = largeList1.stream()
                .filter(largeList2::contains)
                .collect(Collectors.toList());
        long streamTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        List<Integer> intersectionLarge2 = new ArrayList<>(largeList1);
        intersectionLarge2.retainAll(largeList2);
        long collectionTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        Set<Integer> setLarge1 = new HashSet<>(largeList1);
        Set<Integer> setLarge2 = new HashSet<>(largeList2);
        setLarge1.retainAll(setLarge2);
        long setTime = System.currentTimeMillis() - startTime;
        
        System.out.println("大数据集性能比较:");
        System.out.println("Stream API 耗时: " + streamTime + "ms");
        System.out.println("Collection方法耗时: " + collectionTime + "ms");
        System.out.println("Set方法耗时: " + setTime + "ms");
    }
}