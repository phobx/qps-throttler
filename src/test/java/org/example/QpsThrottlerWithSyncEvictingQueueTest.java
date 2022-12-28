package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class QpsThrottlerWithSyncEvictingQueueTest {

    private int numberOfThreads = 20;
    private int durationInMillis = 4900;
    private int averageSleepTimeInMillis = 1;
    private int queriesPerSecond = 10000;
    private final QpsThrottler qpsThrottler = new QpsThrottlerWithSyncEvictingQueue(queriesPerSecond);
    private final AtomicInteger passCount = new AtomicInteger();
    private final AtomicInteger successfulPassCount = new AtomicInteger();

    @Test
    void testPass() {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new QpsThrottlerTestingThread(durationInMillis, averageSleepTimeInMillis, qpsThrottler,
                    passCount, successfulPassCount);
            threads.add(thread);
        }
        Long startTs = System.currentTimeMillis();
        threads.stream().forEach(Thread::start);
        threads.stream().forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException ie) {
                throw new RuntimeException("InterruptedException caught.");
            }
        });
        Long duration = System.currentTimeMillis() - startTs;
        System.out.println("Total time in millis: " + duration);
        System.out.println("Total invocations: " + passCount.get());
        System.out.println("Total successful invocations: " + successfulPassCount.get());
        assertEquals(Math.ceil((double) duration / 1000), successfulPassCount.get() / queriesPerSecond);
    }
}