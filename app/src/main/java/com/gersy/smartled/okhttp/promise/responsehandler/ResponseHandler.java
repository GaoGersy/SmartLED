package com.gersy.smartled.okhttp.promise.responsehandler;

import java.io.IOException;

import okhttp3.Response;

public interface ResponseHandler<T> {
    T handle(Response response) throws IOException;
}
