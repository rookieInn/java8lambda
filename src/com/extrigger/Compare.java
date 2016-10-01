package com.extrigger;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by gxy on 2016/8/18.
 */
public class Compare {

    public static void main(String[] args) {

        final List<Person> people = Arrays.asList(
                new Person("John", 20),
                new Person("Sara", 21),
                new Person("Jane", 21),
                new Person("Greg", 35));

/*        List<Person> ascendingAge = people.stream()
                                          .sorted((person1, person2) -> person1.ageDifference(person2))
                                          .collect(Collectors.toList());*/
        List<Person> ascendingAge = people.stream()
                .sorted(Person::ageDifference)
                .collect(Collectors.toList());
        printPeople("Sorted in ascending order of age: ", ascendingAge);

        //desc order by age
        printPeople("Sorted in descending order of age:",
                people.stream()
                      .sorted((p1,p2) -> p2.ageDifference(p1))
                      .collect(Collectors.toList()));

        //Reusing a Comparator

        Comparator<Person> compareAscending = (p1, p2) -> p1.ageDifference(p2);
        Comparator<Person> compareDescending = compareAscending.reversed();

        printPeople("Sorted in ascending order of age: ",
                people.stream()
                      .sorted(compareAscending)
                      .collect(Collectors.toList()));
        printPeople("Sorted in ascending order of age: ",
                people.stream()
                      .sorted(compareDescending)
                      .collect(Collectors.toList()));

        printPeople("Sorted in ascending order of name: ",
                people.stream()
                        .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                        .collect(Collectors.toList()));

        people.stream().min(Person::ageDifference).ifPresent(youngest -> System.out.println("Youngest: " + youngest));

        people.stream().max(Person::ageDifference).ifPresent(youngest -> System.out.println("Eldest: " + youngest));

        Function<Person, Integer> byAge = person -> person.getAge();
        Function<Person, String> byTheirName = person -> person.getName();

        printPeople("Sorted in ascending order of age and name: ",
                people.stream()
                      .sorted(Comparator.comparing(byAge).thenComparing(byTheirName))
                      .collect(Collectors.toList()));


    }

    public static void printPeople(String message, List<Person> people) {
        System.out.println(message);
        people.forEach(System.out::println);
    }

}
