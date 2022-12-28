package org.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class QpsThrottlerTestingThread extends Thread {
    private final int durationInMillis;
    private final int averageSleepTimeInMillis;
    private final QpsThrottler qpsThrottler;
    private final AtomicInteger passCount;
    private final AtomicInteger successfulPassCount;

    public QpsThrottlerTestingThread(int durationInMillis, int averageSleepTimeInMillis, QpsThrottler qpsThrottler,
                                     AtomicInteger passCount, AtomicInteger successfulPassCount) {
        this.durationInMillis = durationInMillis;
        this.averageSleepTimeInMillis = averageSleepTimeInMillis;
        this.qpsThrottler = qpsThrottler;
        this.passCount = passCount;
        this.successfulPassCount = successfulPassCount;
    }

    @Override
    public void run() {
        long startTs = System.currentTimeMillis();
        long endTs = startTs + durationInMillis;
        while (System.currentTimeMillis() < endTs) {
            try {
                long randSleepTime = ThreadLocalRandom.current().nextLong(0, 2 * averageSleepTimeInMillis + 1);
                Thread.sleep(randSleepTime);
                if (qpsThrottler.pass()) {
                    successfulPassCount.incrementAndGet();
                }
                passCount.incrementAndGet();
            } catch (InterruptedException ie) {
                throw new RuntimeException("InterruptedException caught.");
            }
        }
    }
}
