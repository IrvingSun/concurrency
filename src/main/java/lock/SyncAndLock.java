package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sunwei3 on 2017/3/29.
 *
 * 验证synchronized和ReentrantLock的tryLock、lock方法的阻塞特性。
 *
 *
 */
public class SyncAndLock {
    Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        final SyncAndLock syncAndLock = new SyncAndLock();
        Thread t1 = new Thread("Thread1"){
            @Override
            public void run() {
                try {
//                    syncAndLock.testSync();
//                    syncAndLock.testTryLock();
                    syncAndLock.testLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t2 = new Thread("Thread2"){
            @Override
            public void run() {
                try {
//                    syncAndLock.testSync();
//                    syncAndLock.testTryLock();
                    syncAndLock.testLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();
    }

    private void testSync() throws InterruptedException {
        System.out.println("即将进入testSync方法  "+Thread.currentThread().getName());
        synchronized (this){
            System.out.println("进入testSync方法  "+Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(2);
            System.out.println("退出testSync方法  "+Thread.currentThread().getName());
        }
    }

    private void testTryLock() throws InterruptedException {
        System.out.println("即将进入testTryLock方法  "+Thread.currentThread().getName());
        if(lock.tryLock()){
            try {
                System.out.println("进入testTryLock方法  "+Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(2);
                System.out.println("退出testTryLock方法  "+Thread.currentThread().getName());
            }finally {
                lock.unlock();
            }
        }
    }

    private void testLock() throws InterruptedException {
        System.out.println("即将进入testLock方法  "+Thread.currentThread().getName());
        lock.lock();
           try{
               System.out.println("进入testLock方法  "+Thread.currentThread().getName());
               TimeUnit.SECONDS.sleep(2);
               System.out.println("退出testLock方法  "+Thread.currentThread().getName());
           }finally {
               lock.unlock();
           }
    }
}
