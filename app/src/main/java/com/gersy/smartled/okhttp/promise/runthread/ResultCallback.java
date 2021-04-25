package com.gersy.smartled.okhttp.promise.runthread;

import okhttp3.Response;

public interface ResultCallback {
    void onResult(Response response);
}
