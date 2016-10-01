package com.extrigger;

import java.util.function.Supplier;

/**
 * Created by gxy on 2016/9/6.
 */
public class HolderNaive {

    private Supplier<Heavy> heavy = () -> createAndCacheHeavy();

    //private Heavy heavy;

    public HolderNaive() {
        System.out.println("Holder created");
    }

/*    public Heavy getHeavy() {
        if (heavy == null)
            heavy = new Heavy();

        return heavy;
    }*/

    public /*synchronized*/ Heavy getHeavy() {
        /*if (heavy == null)
            heavy = new Heavy();*/

        return heavy.get();
    }

    private synchronized Heavy createAndCacheHeavy(){
        class HeavyFactory implements Supplier<Heavy>{
            private final Heavy heavyInstance = new Heavy();

            @Override
            public Heavy get() {
                return heavyInstance;
            }
        }

        if (!HeavyFactory.class.isInstance(heavy)){
            heavy = new HeavyFactory();
        }

        return heavy.get();
    }

    public static void main(String[] args) {
        HolderNaive holderNaive = new HolderNaive();
        System.out.println("deferring heavy creation...");
        System.out.println(holderNaive.getHeavy());
        System.out.println(holderNaive.getHeavy());
    }

}
