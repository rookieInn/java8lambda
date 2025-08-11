import java.util.*;

public class ListUtilsExample {
    public static void main(String[] args) {
        // 示例1: 字符串List比较
        System.out.println("=== 字符串List比较 ===");
        List<String> fruits1 = Arrays.asList("apple", "banana", "orange", "grape");
        List<String> fruits2 = Arrays.asList("banana", "grape", "mango", "pear");
        
        System.out.println("List1: " + fruits1);
        System.out.println("List2: " + fruits2);
        
        // 使用工具类获取交集
        List<String> intersection = ListUtils.getIntersection(fruits1, fruits2);
        System.out.println("交集: " + intersection);
        
        // 获取差集
        List<String> difference1 = ListUtils.getDifference(fruits1, fruits2);
        List<String> difference2 = ListUtils.getDifference(fruits2, fruits1);
        System.out.println("List1 - List2: " + difference1);
        System.out.println("List2 - List1: " + difference2);
        
        // 获取并集
        List<String> union = ListUtils.getUnion(fruits1, fruits2);
        System.out.println("并集: " + union);
        
        // 获取对称差集
        List<String> symmetricDiff = ListUtils.getSymmetricDifference(fruits1, fruits2);
        System.out.println("对称差集: " + symmetricDiff);
        
        System.out.println();
        
        // 示例2: 整数List比较
        System.out.println("=== 整数List比较 ===");
        List<Integer> numbers1 = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> numbers2 = Arrays.asList(4, 5, 6, 7, 8, 9);
        
        System.out.println("Numbers1: " + numbers1);
        System.out.println("Numbers2: " + numbers2);
        
        // 使用Set方法（性能更好）
        Set<Integer> intersectionSet = ListUtils.getIntersectionSet(numbers1, numbers2);
        System.out.println("交集(Set): " + intersectionSet);
        
        Set<Integer> differenceSet = ListUtils.getDifferenceSet(numbers1, numbers2);
        System.out.println("差集(Set): " + differenceSet);
        
        System.out.println();
        
        // 示例3: 对象List比较
        System.out.println("=== 对象List比较 ===");
        List<Person> people1 = Arrays.asList(
            new Person("Alice", 25),
            new Person("Bob", 30),
            new Person("Charlie", 35)
        );
        
        List<Person> people2 = Arrays.asList(
            new Person("Bob", 30),
            new Person("David", 40),
            new Person("Eve", 45)
        );
        
        System.out.println("People1: " + people1);
        System.out.println("People2: " + people2);
        
        // 注意：对象比较需要重写equals和hashCode方法
        List<Person> peopleIntersection = ListUtils.getIntersection(people1, people2);
        System.out.println("人员交集: " + peopleIntersection);
        
        System.out.println();
        
        // 示例4: 相等性检查
        System.out.println("=== 相等性检查 ===");
        List<String> listA = Arrays.asList("a", "b", "c");
        List<String> listB = Arrays.asList("c", "b", "a");
        List<String> listC = Arrays.asList("a", "b", "c");
        
        System.out.println("ListA: " + listA);
        System.out.println("ListB: " + listB);
        System.out.println("ListC: " + listC);
        
        System.out.println("A == B (忽略顺序): " + ListUtils.isEqualIgnoreOrder(listA, listB));
        System.out.println("A == B (考虑顺序): " + ListUtils.isEqualWithOrder(listA, listB));
        System.out.println("A == C (考虑顺序): " + ListUtils.isEqualWithOrder(listA, listC));
        
        System.out.println();
        
        // 示例5: 空值处理
        System.out.println("=== 空值处理 ===");
        List<String> emptyList = null;
        List<String> normalList = Arrays.asList("a", "b", "c");
        
        System.out.println("空List与正常List的交集: " + ListUtils.getIntersection(emptyList, normalList));
        System.out.println("正常List与空List的差集: " + ListUtils.getDifference(normalList, emptyList));
    }
    
    // 示例Person类
    static class Person {
        private String name;
        private int age;
        
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Person person = (Person) obj;
            return age == person.age && Objects.equals(name, person.name);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
        
        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }
}