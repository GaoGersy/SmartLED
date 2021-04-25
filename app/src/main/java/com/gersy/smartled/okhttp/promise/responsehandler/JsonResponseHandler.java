package com.gersy.smartled.okhttp.promise.responsehandler;


import com.piesat.outsideinvestigate.utils.JSONUtils;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class JsonResponseHandler<T> implements ResponseHandler<T> {
    private Class<T> clazz;

    public JsonResponseHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T handle(Response response) throws IOException {
        ResponseBody body = response.body();
        String json = body.string();
        return JSONUtils.jsonToBean(json, clazz);
    }
}
