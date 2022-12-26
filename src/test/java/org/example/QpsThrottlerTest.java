package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class QpsThrottlerTest {

    private int numberOfThreads = 5;
    private final QpsThrottler qpsThrottler = new QpsThrottler();
    private final AtomicInteger passCount = new AtomicInteger();
    private final AtomicInteger successfulPassCount = new AtomicInteger();

    @Test
    void testPass() {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Thread thread = new QpsThrottlerTestingThread(qpsThrottler, passCount, successfulPassCount);
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
        Long endTs = System.currentTimeMillis();
        System.out.println("Total time in millis: " + (endTs - startTs));
        System.out.println("Total invocations: " + passCount.get());
        System.out.println("Total successful invocations: " + successfulPassCount.get());
    }
}