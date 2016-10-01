package com.extrigger;

/**
 * Created by gxy on 2016/8/24.
 */
public interface FastFly extends Fly {

    default  void takeOff() {
        System.out.println("FastFly::takeOff");
    }

}
