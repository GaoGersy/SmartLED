package com.gersy.smartled.okhttp;


import com.piesat.outsideinvestigate.okhttp.promise.Promise;
import com.piesat.outsideinvestigate.okhttp.promise.responsehandler.ResponseHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ExternalCall {
    private Call call;

    public ExternalCall(Call call) {
        if (call == null) {
            throw new RuntimeException("传入的call 不能为null");
        }
        this.call = call;
    }

    public <T> Promise<T> toPromise(ResponseHandler<T> responseHandler) {
        return new Promise<>(call, responseHandler);
    }

    /**
     * 同步调用，返回字符串
     */
    public String sync() throws IOException {
        return ResponseHelper.string(response());
    }

    /**
     * 同步调用,返回对象
     */
    public <T> T sync(Class<T> clazz) throws IOException {
        return ResponseHelper.toBean(response(), clazz);
    }

    /**
     * 同步调用，下载文件
     */
    public void sync(File file, DownloadProgress downloadProgress) {
        try {
            ResponseHelper.downFile(response(), file, downloadProgress);
        } catch (IOException e) {
            e.printStackTrace();
            if (downloadProgress!=null){
                downloadProgress.onFailure(e);
            }
        }
    }

    /**
     * 同步调用，返回Response给用户自己处理
     */
    public Response response() throws IOException {
        return call.execute();
    }

    /**
     * 同步调用
     */
    public <T> void async(HttpCallback<T> httpCallback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    Class<T> clazz = getActualTypeArgument(httpCallback.getClass());
                    if (clazz == Object.class || clazz == String.class) {
                        T string = (T) ResponseHelper.string(response);
                        httpCallback.onSuccess(string);
                    } else {
                        T o = ResponseHelper.toBean(response, clazz);
                        httpCallback.onSuccess(o);
                    }
                } catch (Exception e) {
                    httpCallback.onFailure(e);
                }
            }
        });
    }

    /**
     * 异步调用
     *
     * @param file 文件夹或文件，如果为文件夹会从后台返回的header中取文件名
     * @param downloadProgress 下载进度的回调
     */
    public void async(File file, DownloadProgress downloadProgress) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                downloadProgress.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseHelper.downFile(response, file, downloadProgress);
            }
        });
    }

    public <T> Class<T> getActualTypeArgument(Class<?> clazz) {
        Class<T> entitiClass = null;
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        Type genericSuperclass = genericInterfaces[0];
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                entitiClass = (Class<T>) actualTypeArguments[0];
            }
        }

        return entitiClass;
    }

}
