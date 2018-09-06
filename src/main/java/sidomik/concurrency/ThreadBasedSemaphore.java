package sidomik.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadBasedSemaphore implements Semaphore {

    private static final Object TOKEN = new Object();

    private final BlockingQueue<Object> queue;

    public ThreadBasedSemaphore(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size should be >= 1");
        }
        this.queue = new ArrayBlockingQueue<>(size);
    }

    public void acquire() throws InterruptedException {
        queue.put(TOKEN);
    }

    public void release() {
        queue.poll();
    }
}
