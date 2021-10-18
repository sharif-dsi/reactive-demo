package com.bk.nz.reactivedemo.util;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;

}


