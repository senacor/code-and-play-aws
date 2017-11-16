package com.senacor.tecco.ilms.wikiplaneparser.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Simple Wrapper to Block main Thread until async execution finished.
 *
 * @author Andreas Keefer
 */
public class WaitMonitor {

    private static final Logger logger = LoggerFactory.getLogger(WaitMonitor.class);

    private final CountDownLatch countDownLatch;
    private final long creationTimeStamp = System.currentTimeMillis();
    private Long durationToCompleteInMs = null;
    private boolean complete = false;

    public WaitMonitor(int expectedCompletes) {
        this.countDownLatch = new CountDownLatch(expectedCompletes);
    }

    public WaitMonitor() {
        this(1);
    }

    /**
     * Call this Method in your async code when the async execution has finished
     */
    public synchronized void complete() {
        countDownLatch.countDown();
        complete = true;
        durationToCompleteInMs = System.currentTimeMillis() - creationTimeStamp;
    }

    /**
     * Wait with timeout in your main Thread until #complete() is called.
     *
     * @param timeout timeout
     * @param unit    unit
     */
    public void waitFor(long timeout, TimeUnit unit) {
        try {
            countDownLatch.await(timeout, unit);
            synchronized (this) {
                logger.info("runtime to complete: %s ms", durationToCompleteInMs == null ? "-" : durationToCompleteInMs);
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized boolean isComplete() {
        return complete;
    }
}
