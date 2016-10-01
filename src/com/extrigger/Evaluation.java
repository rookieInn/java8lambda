package com.extrigger;

import java.util.function.Supplier;

/**
 * Created by gxy on 2016/9/6.
 */
public class Evaluation {

    public static boolean evaluate(int value) {
        System.out.println("evaluating ..." + value);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return value > 100;
    }

    public static void eagerEvaluator(final boolean input1, final boolean input2) {
        System.out.println("eagerEvaluator called...");
        System.out.println("accept?: " + (input1 && input2));
    }

    public static void lazyEvaluator(Supplier<Boolean> input1, Supplier<Boolean> input2){
        System.out.println("lazyEvaluator called ...");
        System.out.println("accept?:" + (input1.get() && input2.get()));

    }

    public static void main(String[] args) {
        //eagerEvaluator(evaluate(1), evaluate(2));
        lazyEvaluator(() -> evaluate(1), () -> evaluate(2));
    }

}
