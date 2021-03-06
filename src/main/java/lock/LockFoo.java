package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sunwei3 on 2017/3/27.
 */
public class LockFoo {
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        System.out.println("lock state is:" + lock);
        try {
            System.out.println("lock state is:" + lock);
        } finally {
            System.out.println("lock state is:" + lock);
            lock.unlock();
            System.out.println("lock state is:" + lock);
        }

    }
}
