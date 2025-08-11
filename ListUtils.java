import java.util.*;
import java.util.stream.Collectors;

/**
 * List比较工具类
 * 提供交集、差集、并集等操作
 */
public class ListUtils {
    
    /**
     * 获取两个List的交集
     * @param list1 第一个List
     * @param list2 第二个List
     * @param <T> 泛型类型
     * @return 交集List
     */
    public static <T> List<T> getIntersection(List<T> list1, List<T> list2) {
        if (list1 == null || list2 == null) {
            return new ArrayList<>();
        }
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取list1相对于list2的差集
     * @param list1 第一个List
     * @param list2 第二个List
     * @param <T> 泛型类型
     * @return 差集List
     */
    public static <T> List<T> getDifference(List<T> list1, List<T> list2) {
        if (list1 == null) {
            return new ArrayList<>();
        }
        if (list2 == null) {
            return new ArrayList<>(list1);
        }
        return list1.stream()
                .filter(item -> !list2.contains(item))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取两个List的并集
     * @param list1 第一个List
     * @param list2 第二个List
     * @param <T> 泛型类型
     * @return 并集List
     */
    public static <T> List<T> getUnion(List<T> list1, List<T> list2) {
        Set<T> unionSet = new LinkedHashSet<>();
        if (list1 != null) {
            unionSet.addAll(list1);
        }
        if (list2 != null) {
            unionSet.addAll(list2);
        }
        return new ArrayList<>(unionSet);
    }
    
    /**
     * 获取两个List的对称差集（并集减去交集）
     * @param list1 第一个List
     * @param list2 第二个List
     * @param <T> 泛型类型
     * @return 对称差集List
     */
    public static <T> List<T> getSymmetricDifference(List<T> list1, List<T> list2) {
        List<T> difference1 = getDifference(list1, list2);
        List<T> difference2 = getDifference(list2, list1);
        
        List<T> symmetricDifference = new ArrayList<>();
        symmetricDifference.addAll(difference1);
        symmetricDifference.addAll(difference2);
        return symmetricDifference;
    }
    
    /**
     * 使用Set方法获取交集（性能更好）
     * @param list1 第一个List
     * @param list2 第二个List
     * @param <T> 泛型类型
     * @return 交集Set
     */
    public static <T> Set<T> getIntersectionSet(List<T> list1, List<T> list2) {
        if (list1 == null || list2 == null) {
            return new HashSet<>();
        }
        Set<T> set1 = new HashSet<>(list1);
        Set<T> set2 = new HashSet<>(list2);
        set1.retainAll(set2);
        return set1;
    }
    
    /**
     * 使用Set方法获取差集（性能更好）
     * @param list1 第一个List
     * @param list2 第二个List
     * @param <T> 泛型类型
     * @return 差集Set
     */
    public static <T> Set<T> getDifferenceSet(List<T> list1, List<T> list2) {
        if (list1 == null) {
            return new HashSet<>();
        }
        if (list2 == null) {
            return new HashSet<>(list1);
        }
        Set<T> set1 = new HashSet<>(list1);
        Set<T> set2 = new HashSet<>(list2);
        set1.removeAll(set2);
        return set1;
    }
    
    /**
     * 检查两个List是否相等（忽略顺序）
     * @param list1 第一个List
     * @param list2 第二个List
     * @param <T> 泛型类型
     * @return 是否相等
     */
    public static <T> boolean isEqualIgnoreOrder(List<T> list1, List<T> list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        
        Set<T> set1 = new HashSet<>(list1);
        Set<T> set2 = new HashSet<>(list2);
        return set1.equals(set2);
    }
    
    /**
     * 检查两个List是否相等（考虑顺序）
     * @param list1 第一个List
     * @param list2 第二个List
     * @param <T> 泛型类型
     * @return 是否相等
     */
    public static <T> boolean isEqualWithOrder(List<T> list1, List<T> list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        return list1.equals(list2);
    }
}