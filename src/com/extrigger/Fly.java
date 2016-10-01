package com.extrigger;

/**
 * Created by gxy on 2016/8/24.
 */
public interface Fly {

    default void takeOff() {
        System.out.println("Fly::takeOff");
    }

    default void land() {
        System.out.println("Fly::land");
    }

    default void turn() {
        System.out.println("Fly::turn");
    }

    default void cruise() {
        System.out.println("Fly::curise");
    }

}
