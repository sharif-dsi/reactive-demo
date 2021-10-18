package com.bk.nz.reactivedemo.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadConfigUtil {
    private static final Long PER_THREAD_MEMORY_IN_BYTES = 1024L * 1024L;
    private static final Double MEMORY_FRACTION_FOR_ACTIVE_THREAD = 1.0 / 8.0;
    private static final Long KEEP_ALIVE_TIME_AFTER_EXCEED = 30L;

    public static ExecutorService getStandardExecutorService() {
        long maxJvmMemory = Runtime.getRuntime().maxMemory();
        log.info("maxJvmMemory = {}", maxJvmMemory);
        long memoryAllocationForActiveThread = (long) ((double) maxJvmMemory * MEMORY_FRACTION_FOR_ACTIVE_THREAD);
        var maxActiveThreadCount = (int) (memoryAllocationForActiveThread / PER_THREAD_MEMORY_IN_BYTES);
        var maxReserveThreadCount = maxActiveThreadCount * 2;
        log.info("maxActiveThreadCount = {}, maxReserveThreadCount = {}", maxActiveThreadCount, maxReserveThreadCount);

        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(maxReserveThreadCount * 100);

        return new ThreadPoolExecutor(
                maxActiveThreadCount,
                maxReserveThreadCount,
                KEEP_ALIVE_TIME_AFTER_EXCEED,
                TimeUnit.MILLISECONDS,
                blockingQueue
        );
    }
}
