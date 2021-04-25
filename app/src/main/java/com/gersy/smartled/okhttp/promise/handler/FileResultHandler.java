package com.gersy.smartled.okhttp.promise.handler;

import java.io.File;

public interface FileResultHandler<T> extends ResultHandler<File,T> {
    void onProgress(int progress);
}
