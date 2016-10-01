package com.extrigger;

/**
 * Created by gxy on 2016/8/24.
 */
public interface Sail {

    default void turn() {
        System.out.println("Sail::turn");
    }

    default void cruise() {
        System.out.println("Sail::cruise");
    }

}
