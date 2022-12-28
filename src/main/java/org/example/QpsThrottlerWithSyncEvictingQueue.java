package org.example;

import com.google.common.collect.EvictingQueue;

public class QpsThrottlerWithSyncEvictingQueue extends QpsThrottler {

    private final static long MILLIS_IN_SECOND = 1000L;
    private final EvictingQueue<Long> evictingQueue;

    public QpsThrottlerWithSyncEvictingQueue(int limitPerSecond) {
        super(limitPerSecond);
        this.evictingQueue = EvictingQueue.create(limitPerSecond);
    }

    public QpsThrottlerWithSyncEvictingQueue() {
        this(DEFAULT_LIMIT);
    }

    @Override
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
