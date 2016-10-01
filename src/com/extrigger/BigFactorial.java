package com.extrigger;

import java.math.BigInteger;

/**
 * Created by gxy on 2016/9/7.
 */
public class BigFactorial {

    final static BigInteger ONE = BigInteger.ONE;
    final static BigInteger FIVE = new BigInteger("5");
    final static BigInteger TWENTYK = new BigInteger("20000");

    public static BigInteger decrement(BigInteger number) {
        return number.subtract(BigInteger.ONE);
    }

    public static BigInteger multiply(BigInteger first, BigInteger second) {
        return first.multiply(second);
    }

    public static BigInteger factorial(final BigInteger number) {
        return factorialTailRec(BigInteger.ONE, number).invoke();
    }

    public static TailCall<BigInteger> factorialTailRec(BigInteger factorial, BigInteger number) {
        if (number.equals(BigInteger.ONE))
            return TailCalls.done(factorial);
        else
            return TailCalls.call(() -> factorialTailRec(BigFactorial.multiply(factorial, number), BigFactorial.decrement(number)));
    }

    public static void main(String[] args) {
      /*  System.out.println(factorial(FIVE));
        System.out.println(factorial(TWENTYK));*/
        System.out.println(sum(5, 0));
    }

    public static int sum(int a, int b) {
        if (a == 0)
            return b;
        else
            return sum(a - 1, a + b);
    }

}
