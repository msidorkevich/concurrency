package sidomik.concurrency;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ThreadBasedSemaphoreTest {

    @Test
    public void releaseEmpty() {
        Semaphore semaphore = new ThreadBasedSemaphore(1);
        semaphore.release();
    }

    @Test
    public void releaseAcquired() throws InterruptedException {
        Semaphore semaphore = new ThreadBasedSemaphore(1);
        semaphore.acquire();
        semaphore.release();
    }

    @Test //(timeout = 10000)
    public void acquireFull() throws InterruptedException {
        final Semaphore semaphore = new ThreadBasedSemaphore(2);

        System.out.println("Acquire inside thread " + Thread.currentThread().getName());
        semaphore.acquire();

        final CountDownLatch firstAcquired = new CountDownLatch(1);
        new Thread(new Runnable() {
            public void run() {
                try {
                    semaphore.acquire();
                    firstAcquired.countDown();
                } catch (InterruptedException e) {
                }
            }
        }).start();

        firstAcquired.await();

        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            public void run() {
                try {
                    semaphore.acquire();
                    latch.countDown();
                } catch (InterruptedException e) {
                }
            }
        }).start();

        assertFalse(latch.await(2, TimeUnit.SECONDS));
    }
    
    @Test (timeout = 10000)
    public void acquireFullFromSameThread() throws InterruptedException {
        final Semaphore semaphore = new ThreadBasedSemaphore(1);

        semaphore.acquire();
        semaphore.acquire();
    }
}