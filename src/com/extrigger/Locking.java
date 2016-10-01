package com.extrigger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gxy on 2016/8/25.
 */
public class Locking {
    Lock lock = new ReentrantLock();

    protected void setLock(Lock mock) {
        lock = mock;
    }

    public void doOp1() {
        lock.lock();
        try{
           //... critical code
        } finally {
          lock.unlock();
        }
    }

    public void doOp2() {
        Locker.runLocked(lock, () -> {/*  code.. */});
    }

    public void doOp3() {
        Locker.runLocked(lock, () -> {/*  code.. */});
    }

    public void doOp4() {
        Locker.runLocked(lock, () -> {/*  code.. */});
    }

}
