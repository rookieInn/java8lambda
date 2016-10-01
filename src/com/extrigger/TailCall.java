package com.extrigger;

import java.util.stream.Stream;

/**
 * Created by gxy on 2016/9/7.
 */
@FunctionalInterface
public interface TailCall<T> {

    TailCall<T> apply();

    default boolean isComplete() {
        return false;
    }

    default T result() {
        throw new Error("not implemented");
    }

    default T invoke() {
        return Stream.iterate(this, TailCall::apply)
                      .filter(TailCall::isComplete)
                      .findFirst()
                      .get()
                      .result();
    }

}
