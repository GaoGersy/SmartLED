package com.gersy.smartled.okhttp.promise.handler;


import com.gersy.smartled.okhttp.promise.Promise;

public interface ResultHandler<T, E> {

    Promise<E> resolve(T result);

    void reject(Exception e);
}
