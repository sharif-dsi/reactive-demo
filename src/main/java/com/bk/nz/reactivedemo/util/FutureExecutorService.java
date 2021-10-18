package com.bk.nz.reactivedemo.util;

import java.util.concurrent.CompletableFuture;

public interface FutureExecutorService {

     <T> CompletableFuture<T> execute(ThrowingSupplier<T, Exception> tExceptionThrowingSupplier);

}
