package org.example;

import com.google.common.collect.EvictingQueue;

public class QpsThrottler {

    private final static int DEFAULT_LIMIT = 100;
    private final static long MILLIS_IN_SECOND = 1000L;
    private final int limitPerSecond;
    private final EvictingQueue<Long> evictingQueue;

    public QpsThrottler(int limitPerSecond) {
        if (limitPerSecond < 1)
            throw new IllegalArgumentException("Limit per second should be more than 0");
        this.limitPerSecond = limitPerSecond;
        this.evictingQueue = EvictingQueue.create(limitPerSecond);
    }

    public QpsThrottler() {
        this(DEFAULT_LIMIT);
    }

    public boolean pass() {
        synchronized (evictingQueue) {
            Long now = System.currentTimeMillis();
            boolean queueIsNotFull = evictingQueue.size() < limitPerSecond;
            if (queueIsNotFull) {
                evictingQueue.add(now);
                return true;
            } else {
                Long oldestStoredCall = evictingQueue.peek();
                boolean longerThanASecondAgo = now - oldestStoredCall >= MILLIS_IN_SECOND;
                if (longerThanASecondAgo) {
                    evictingQueue.add(now);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
