package com.gersy.smartled.okhttp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieHandler implements CookieJar {
    private static final Map<String, List<Cookie>> cookieStore = new HashMap<>();
    private static final Map<String, CookieJar> cookieHandler = new HashMap<>();
    private static final List<Cookie> empty = new ArrayList<>(0);

    
    @Override
    public List<Cookie> loadForRequest( HttpUrl httpUrl) {
        CookieJar cookieJar = cookieHandler.get(httpUrl.host());
        //需要发起请求者自己处理的情况
        if (cookieJar !=null){
            return cookieJar.loadForRequest(httpUrl);
        }else {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            return cookies != null ? cookies : empty;
        }
    }

    @Override
    public void saveFromResponse( HttpUrl httpUrl,  List<Cookie> list) {
        CookieJar cookieJar = cookieHandler.get(httpUrl.host());
        //需要发起请求者自己处理的情况
        if (cookieJar !=null){
            cookieJar.saveFromResponse(httpUrl,list);
        }else {
            cookieStore.put(httpUrl.host(), list);
        }
    }

    public void registerHandler(String host, CookieJar cookieJar){
        cookieHandler.put(host,cookieJar);
    }

    public void unRegisterHandler(String host){
        cookieHandler.remove(host);
    }
}
