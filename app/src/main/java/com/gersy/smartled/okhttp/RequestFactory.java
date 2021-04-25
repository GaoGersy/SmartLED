package com.gersy.smartled.okhttp;


import com.gersy.smartled.utils.JSONUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestFactory {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM_DATA = MediaType.parse("multipart/form-data; charset=utf-8");
    private static final MediaType UPLOAD_FILE = MediaType.parse("application/octet-stream");

    public static Request getRequest() {
        return null;
    }

    public static Request get(String url){
        return new Request.Builder().url(url).build();
    }

    /**
     * post form表单数据
     * @param url
     * @param params
     * @return
     */
    public static Request postForm(String url, Map<String,String> params){
        FormBody.Builder formBody = new FormBody.Builder();
        if (params!=null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                formBody.add(entry.getKey(), entry.getValue());
            }
        }
        return postRequest(url, formBody.build());
    }

    /**
     * post json数据
     * @param url
     * @param obj
     * @return
     */
    public static Request postJson(String url, Object obj){
        RequestBody requestBody = RequestBody.create(JSONUtils.beanToJson(obj),JSON);
        return postRequest(url, requestBody);
    }

    /**
     * post json数据
     * @param url
     * @param json
     * @return
     */
    public static Request postJson(String url, String json){
        RequestBody requestBody = RequestBody.create(json,JSON);
        return postRequest(url, requestBody);
    }

    /**
     * 上传多文件
     * @param url
     * @param files
     * @return
     */
    public static Request postFiles(String url, List<File> files){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (File file : files) {
            builder.addFormDataPart("files",file.getName(), RequestBody.create(file,UPLOAD_FILE));
        }
        return postRequest(url,builder.build());
    }

    /**
     * 上传单个文件
     * @param url
     * @param file
     * @return
     */
    public static Request postFile(String url, File file){
        MultipartBody.Builder builder = new MultipartBody.Builder()
            .addFormDataPart("file",file.getName(), RequestBody.create(file,UPLOAD_FILE));
        return postRequest(url,builder.build());
    }

    private static Request postRequest(String url, RequestBody requestBody) {
        return new Request.Builder().url(url).post(requestBody).build();
    }


}
