package sidomik.concurrency;

public interface Semaphore {

    void acquire() throws InterruptedException;
    void release();
}
