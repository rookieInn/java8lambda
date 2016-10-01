package com.extrigger;

import java.util.concurrent.locks.Lock;

/**
 * Created by gxy on 2016/8/25.
 */
public class Locker {

    public static void runLocked(Lock lock, Runnable block) {
        lock.lock();
        try {
            block.run();
        } finally {
            lock.unlock();
        }
    }

}
