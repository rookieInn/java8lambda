package com.extrigger;

/**
 * Created by gxy on 2016/8/25.
 */
@FunctionalInterface
public interface UseInstance<T, X extends Throwable> {

    void accept(T instance) throws X;

}
