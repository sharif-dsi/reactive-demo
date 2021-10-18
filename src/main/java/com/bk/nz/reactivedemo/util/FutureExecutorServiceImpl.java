package com.bk.nz.reactivedemo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

@Slf4j
@Component
public class FutureExecutorServiceImpl implements FutureExecutorService {

    private final ExecutorService executorService;

    public FutureExecutorServiceImpl(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public <T> CompletableFuture<T> actuallyExecute(Supplier<T> fn) {
        var future = new CompletableFuture<T>();
        Runnable runnable = () -> {
            try {
                var value = fn.get();
                future.complete(value);
            } catch (RuntimeException ex) {
                log.error(ex.getLocalizedMessage(), ex);
                future.completeExceptionally(ex);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage(), ex);
                future.completeExceptionally(new RuntimeException("Generic Exception"));
            }
        };

        executorService.submit(runnable);
        return future;
    }

    static <T> Supplier<T> wrapper(
            ThrowingSupplier<T, Exception> tExceptionThrowingSupplier) {

        return () -> {
            try {
                return tExceptionThrowingSupplier.get();
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    public <T> CompletableFuture<T> execute(ThrowingSupplier<T, Exception> tExceptionThrowingSupplier) {
        return actuallyExecute(wrapper(tExceptionThrowingSupplier));
    }
}
