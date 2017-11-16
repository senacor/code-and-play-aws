package com.senacor.tecco.ilms.wikiloader.service.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Andreas Keefer
 */
@Component
public class DelayUtil {

    private static final Logger logger = LoggerFactory.getLogger(DelayUtil.class);

    private final ExecutorService executorService;

    public DelayUtil() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        logger.info("running delay with availableProcessors: " + availableProcessors);
        executorService = Executors.newFixedThreadPool(availableProcessors);
    }

    public void delay(int milis){
        long start = System.currentTimeMillis();
        List<Future> res = new ArrayList<>();
        for (int i = 0; i < milis / 10; i++) {
            res.add(executorService.submit(() -> burnCpu(31)));
        }
        res.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.warn("burn cpu failure", e);
            }
        });
        logger.info("delayed " + (System.currentTimeMillis() - start) + "ms");
    }

    private void burnCpu(int milliseconds) {
        long sleepTime = milliseconds * 1000000L; // convert to nanoseconds
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < sleepTime) {
        }
    }
}
