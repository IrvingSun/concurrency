package lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sunwei3 on 2017/3/27.
 * 在多线程环境下，演示独占锁的获取
 */
public class MultiThreadFoo{
    static Lock lock = new ReentrantLock();
    static CountDownLatch cdl = new CountDownLatch(1);
    static volatile int count = 1;

    public static void main(String[] args){
        for(int i =0;i<5;i++){
          Thread thread = new Thread("Thread-"+i){
              @Override
              public void run() {
                  try {
                      cdl.await();
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  doWork();
              }
          };
          thread.start();
        }
        System.out.println("===========start==========");
        cdl.countDown();
    }

    private static void doWork(){
        lock.lock();
        try {
            count = count + 1;
            System.out.println("count 被 "+Thread.currentThread().getName()+"  +1,"+count);
        } finally {
            lock.unlock();
        }
    }

}
