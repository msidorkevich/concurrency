package sidomik.concurrency;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;

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

    @Test (timeout = 10000)
    public void acquireFull() throws InterruptedException {
        final Semaphore semaphore = new ThreadBasedSemaphore(2);

        System.out.println("Acquire inside thread " + Thread.currentThread().getName());
        semaphore.acquire();

        final CountDownLatch firstAcquired = new CountDownLatch(1);
        new Thread(() -> {
            try {
                semaphore.acquire();
                firstAcquired.countDown();
            } catch (InterruptedException e) {
            }
        }).start();

        firstAcquired.await();

        final CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            try {
                semaphore.acquire();
                latch.countDown();
            } catch (InterruptedException e) {
            }
        }).start();

        assertFalse(latch.await(2, TimeUnit.SECONDS));
    }
}