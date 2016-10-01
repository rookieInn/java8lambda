package com.extrigger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Reducing a Collection to a Single Value
 *
 * Created by gxy on 2016/8/16.
 */
public class PickALongest {

    public static void main(String[] args) {
        final List<String> friends = Arrays.asList("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        System.out.println("Total number of characters in all names: " + friends.stream().mapToInt(name -> name.length()).sum());

        final Optional<String> aLongName = friends.stream()
                                                   .reduce((name1, name2) -> name1.length() >= name2.length() ? name1 : name2);
        aLongName.ifPresent(name -> System.out.println(String.format("A long name: %s", name)));

        final String steveOrLonger = friends.stream().reduce("Steve", (a, b) -> a.length() >= b.length() ? a :b);
        System.out.println(steveOrLonger);
    }

}
