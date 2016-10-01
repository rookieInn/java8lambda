package com.extrigger;

import java.math.BigInteger;

/**
 * Created by gxy on 2016/9/7.
 */
public class Factorial {

    public static int factorialRec(int number) {
        if (number == 1)
            return number;
        else
            return number * factorialRec(number - 1);
    }

    public static TailCall<Integer> factorialTailRec(int factorial, int number) {
        if (number == 1)
            return TailCalls.done(factorial);
        else
            return TailCalls.call(() -> factorialTailRec(factorial * number, number - 1));
    }

    public static int factorial(final int number) {
        return factorialTailRec(1, number).invoke();
    }


    public static void main(String[] args) {
        try {
            System.out.println(factorialTailRec(1, 20000).invoke());

            //System.out.println(factorialRec(20000));
        } catch (StackOverflowError e) {
            System.err.println(e);
        }
    }


}
