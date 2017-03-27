package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by sunwei3 on 2017/3/27.
 */
public class Mutex implements Lock{
    private static class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean isHeldExclusively() {
            return getState() == -1;
        }

        @Override
        protected boolean tryAcquire(int acquires) {
            System.out.println("开始获取独占式锁...");
            if(compareAndSetState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
                System.out.println("获取独占式锁完成...");
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int release) {
            System.out.println("开始释放锁...");
            if(getState() == 0){
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            System.out.println("释放锁完成...");
            return true;
        }

        Condition newCondition(){
            return new ConditionObject();
        }

        Thread getOwner(){
            return getExclusiveOwnerThread();
        }
    }

    private final Sync sync = new Sync();

    public void lock() {
        sync.acquire(1);
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    public void unlock() {
        sync.release(1);
    }

    public Condition newCondition() {
        return newCondition();
    }

    public String toString() {
        Thread o = sync.getOwner();
        return super.toString() + ((o == null) ?
                "[Unlocked]" :
                "[Locked by thread " + o.getName() + "]");
    }
}
