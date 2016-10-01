package com.extrigger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by gxy on 2016/8/20.
 */
public class OlderThan20 {

    public static void main(String[] args) {
        final List<Person> people = Arrays.asList(
                new Person("John", 20),
                new Person("Sara", 21),
                new Person("Jane", 21),
                new Person("Greg", 35));

        //collect the only people older than 20 years
        List<Person> olderThan20 = new ArrayList<>();
        people.stream().filter(person -> person.getAge() > 20).forEach(person -> olderThan20.add(person));
        System.out.println("People older than 20: " + olderThan20);

        List<Person> olderThan201 = people.stream()
                                          .filter(person -> person.getAge() > 20)
                                          .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println("People older than 20: " + olderThan201);

        List<Person> olderThan202= people.stream()
                                          .filter(person -> person.getAge() > 20)
                                          .collect(Collectors.toList());
        System.out.println("People older than 20: " + olderThan202);

        /*
        Collectors
            toList()
            toSet()
            toMap()
            joining()
            mapping()
            collectionAndThen()
            minBy()
            maxBy()
            groupingBy()
         */
        Map<Integer, List<Person>> peopleByAge =
                people.stream()
                      .collect(Collectors.groupingBy(Person::getAge));
        System.out.println("People grouped by age: " + peopleByAge);

        //get people's names, order by age
        Map<Integer, List<String>> nameOfPeopleByAge = people.stream()
                                                             .collect(Collectors.groupingBy(Person::getAge, Collectors.mapping(Person::getName, Collectors.toList())));
        System.out.println("People grouped by age: " + nameOfPeopleByAge);


        Comparator<Person> byAge = Comparator.comparing(Person::getAge);

        Map<Character, Optional<Person>> oldestPersonInEachAlphabet = people.stream()
                .collect(Collectors.groupingBy(person -> person.getName().charAt(0), Collectors.reducing(BinaryOperator.maxBy(byAge))));
        System.out.println("Oldest person in each alphabet:");
        System.out.println(oldestPersonInEachAlphabet);

    }

}
