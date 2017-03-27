package lock;

import java.util.concurrent.locks.Lock;

/**
 * Created by sunwei3 on 2017/3/27.
 */
public class MutexFoo {
    private static Lock lock = new Mutex();

    public static void main(String[] args) {
        System.out.println("lock state is:" + lock);
        lock.tryLock();
        try {
            System.out.println("lock state is:" + lock);
        } finally {
            lock.unlock();
            System.out.println("lock state is:" + lock);
        }

    }
}
