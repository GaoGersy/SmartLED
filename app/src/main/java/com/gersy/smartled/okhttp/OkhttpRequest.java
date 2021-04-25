package com.gersy.smartled.okhttp;



import com.gersy.smartled.okhttp.promise.responsehandler.StringResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public final class OkhttpRequest {

    private static final String TAG = "OkHttpUtils";
    private static final long DEFAULT_MILLISECONDS = 600000;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM_DATA = MediaType.parse("multipart/form-data; charset=utf-8");

    private static OkhttpRequest mInstance;
    private OkHttpClient mOkHttpClient;
    private HttpLoggingInterceptor loggingInterceptor;
    private CookieHandler cookieJar;

    public static final StringResponseHandler STRING_RESPONSE_HANDLER = new StringResponseHandler();

    private OkhttpRequest() {
        initOkHttp("gersy");
    }

    protected void initOkHttp(String tag) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //添加OkGo默认debug日志
        builder.addInterceptor(loggingInterceptor);
        setTimeout(builder);

        cookieJar = new CookieHandler();
        builder.cookieJar(cookieJar);
        mOkHttpClient = builder.build();
    }

    private void setTimeout(OkHttpClient.Builder builder) {
        //超时时间设置，默认60秒
        //全局的读取超时时间
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
    }

    /**
     * 通过单例模式构造对象
     *
     * @return OkHttpUtils
     */
    private static OkhttpRequest getInstance() {
        if (mInstance == null) {
            synchronized (OkhttpRequest.class) {
                if (mInstance == null) {
                    mInstance = new OkhttpRequest();
                }
            }
        }
        return mInstance;
    }

    public static CookieHandler getCookieHandler(){
        return getInstance().cookieJar;
    }

    /**
     * get请求
     */
    public static ExternalCall get(String url) {
        Request request = RequestFactory.get(url);
        return toExternalCall(request);
    }

    /**
     * post对象json
     */
    public static ExternalCall postJson(String url, Object obj) {
        Request request = RequestFactory.postJson(url, obj);
        return toExternalCall(request);
    }

    /**
     * form表单提交
     */
    public static ExternalCall postForm(String url, Map<String, String> params) {
        Request request = RequestFactory.postForm(url, params);
        return toExternalCall(request);
    }

    /**
     * 上传单个文件
     */
    public static ExternalCall postFile(String url, File file) {
        Request request = RequestFactory.postFile(url, file);
        return toExternalCall(request);
    }

    /**
     * 上传多个文件
     *
     * @param files 文件集合
     */
    public static ExternalCall postFiles(String url, List<File> files) {
        Request request = RequestFactory.postFiles(url, files);
        return toExternalCall(request);
    }

    public static Call getCall(String url){
        Request request = RequestFactory.get(url);
        return getCall(request);
    }

    public static Call getCall(Request request){
        return getInstance().mOkHttpClient.newCall(request);
    }
    
    private static ExternalCall toExternalCall(Request request) {
        return new ExternalCall(getInstance().mOkHttpClient.newCall(request));
    }

    public void async() {
//        get("").async();
    }

    public void sync() {
        try {
            get("").sync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
