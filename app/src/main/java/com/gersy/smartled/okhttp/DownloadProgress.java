package com.gersy.smartled.okhttp;

import java.io.File;

public interface DownloadProgress extends HttpCallback<File> {
    /**
     * 下载进度回调
     * @param progress 进度
     */
    void onProgress(int progress);

}
