package com.gersy.smartled.okhttp;

public interface HttpCallback<T> {
    /**
     * 请求成功回调
     */
    void onSuccess(T result);

    /**
     * 请求失败回调
     */
    void onFailure(Exception e);
}
