package org.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class QpsThrottlerTestingThread extends Thread {
    private final int durationInSeconds;
    private final int averageSleepTimeInMillis;
    private final QpsThrottler qpsThrottler;
    private final AtomicInteger passCount;
    private final AtomicInteger successfulPassCount;

    public QpsThrottlerTestingThread(QpsThrottler qpsThrottler, AtomicInteger passCount, AtomicInteger successfulPassCount) {
        this.durationInSeconds = 5;
        this.averageSleepTimeInMillis = 10;
        this.passCount = passCount;
        this.successfulPassCount = successfulPassCount;
        this.qpsThrottler = qpsThrottler;
    }

    @Override
    public void run() {
        Long startTs = System.currentTimeMillis();
        Long endTs = startTs + durationInSeconds * 1000;
        while (System.currentTimeMillis() < endTs) {
            try {
                Long randSleepTime = ThreadLocalRandom.current().nextLong(0, 2 * averageSleepTimeInMillis + 1);
                System.out.println(randSleepTime);
                Thread.sleep(randSleepTime);
                if (qpsThrottler.pass()) {
                    successfulPassCount.incrementAndGet();
                }
                passCount.incrementAndGet();
            } catch (InterruptedException ie) {
                throw new RuntimeException("InterruptedException caught.");
            }
        }
        System.out.println("Thread was working for " + (System.currentTimeMillis() - startTs) + " ms");
    }
}
