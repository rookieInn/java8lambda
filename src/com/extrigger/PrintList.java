package com.extrigger;

import java.util.Arrays;
import java.util.List;

/**
 * Joining Elements
 *
 * Created by gxy on 2016/8/16.
 */
public class PrintList {

    public static void main(String[] args) {
        final List<String> friends = Arrays.asList("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        for (String name : friends) {
            System.out.print(name + ", ");
        }
        System.out.println();

        for(int i = 0; i < friends.size() - 1; i++) {
            System.out.print(friends.get(i) + ", ");
        }
        if(friends.size() > 0)
            System.out.println(friends.get(friends.size() - 1));

        System.out.println(String.join(", ", friends));
    }

}
