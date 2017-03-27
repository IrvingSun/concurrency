package lock;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sunwei3 on 2017/3/27.
 */
public class ConditionFoo {

    public static void main(String[] args) throws InterruptedException {
        final BoundedQueue<String> bq = new BoundedQueue<String>(5);
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        bq.add("XXXX");
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                condition.signal();
                lock.unlock();
            }
        },3,TimeUnit.SECONDS);

        lock.lock();
        condition.await();
        System.out.println(bq.remove());
        lock.unlock();

//        for(int i=0; i< 4;i++){
//            Thread thread = null ;
//            if(i/2==0){
//                thread = new Thread(){
//                    @Override
//                    public void run() {
//                       for(int j=0;j<5;j++){
//                           try {
//                               bq.add("add-"+j);
//                               System.out.println("添加一条数据"+" add-"+j);
//                               TimeUnit.SECONDS.sleep(1);
//                           } catch (InterruptedException e) {
//                               e.printStackTrace();
//                           }
//                       }
//                    }
//                };
//            }else{
//                thread = new Thread(){
//                    @Override
//                    public void run() {
//                        for(int j=0;j<5;j++){
//                            try {
//                                System.out.println("删除一条数据 "+bq.remove());
//                                TimeUnit.SECONDS.sleep(1);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                };
//            }
//            thread.start();
//        }

    }

    private static class BoundedQueue<T>{
        private Object[] items;

        private int addIndex,removeIndex,count;

        private Lock lock = new ReentrantLock();
        private Condition full = lock.newCondition();
        private Condition notfull = lock.newCondition();

        public BoundedQueue (int size){
            items = new Object[size];
        }

        public void add(T t) throws InterruptedException {
            lock.lock();

            try {
                while (count == items.length)
                    notfull.await();

                items[addIndex] = t;
                if(++addIndex == items.length)
                    addIndex = 0;
                ++count;
                full.signal();
            } finally {
                lock.unlock();
            }
        }

        public T remove() throws InterruptedException {
            lock.lock();

            try {
                while(count == 0)
                    full.await();

                Object x = items[removeIndex];
                items[removeIndex] = null;
                if(++removeIndex == items.length){
                    removeIndex = 0;
                }
                --count;
                notfull.signal();
                return (T)x;
            } finally {
                lock.unlock();
            }


        }


    }
}
