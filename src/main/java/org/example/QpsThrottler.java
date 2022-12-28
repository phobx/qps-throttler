package org.example;

public abstract class QpsThrottler {

    public final static int DEFAULT_LIMIT = 100;
    protected final int limitPerSecond;

    public QpsThrottler(int limitPerSecond) {
        if (limitPerSecond < 1)
            throw new IllegalArgumentException("Limit per second should be more than 0");
        this.limitPerSecond = limitPerSecond;
    }

    public QpsThrottler() {
        this(DEFAULT_LIMIT);
    }

    public abstract boolean pass();
}
