package sidomik.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadBasedSemaphore implements Semaphore {

    private final BlockingQueue<Thread> queue;

    public ThreadBasedSemaphore(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size should be >= 1");
        }
        this.queue = new ArrayBlockingQueue<Thread>(size);
    }

    public void acquire() throws InterruptedException {
        Thread currentThread = Thread.currentThread();

        synchronized (queue) {
            if (!queue.contains(currentThread)) {
                queue.put(currentThread);
            }
        }
    }

    public void release() {
        if (queue.size() == 0) {
            return;
        }

        synchronized (queue) {
            if (queue.size() == 0) {
                return;
            }
            queue.remove(Thread.currentThread());
        }
    }
}
