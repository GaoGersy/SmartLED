package com.gersy.smartled.okhttp.promise.responsehandler;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class StringResponseHandler implements ResponseHandler<String> {

    @Override
    public String handle(Response response) throws IOException {
        ResponseBody body = response.body();
        return body.string();
    }
}
