package com.gersy.smartled.okhttp.promise;


import com.piesat.outsideinvestigate.okhttp.DownloadProgress;
import com.piesat.outsideinvestigate.okhttp.HttpCallback;
import com.piesat.outsideinvestigate.okhttp.OkhttpRequest;
import com.piesat.outsideinvestigate.okhttp.promise.handler.FileResultHandler;
import com.piesat.outsideinvestigate.okhttp.promise.handler.ResultHandler;
import com.piesat.outsideinvestigate.okhttp.promise.responsehandler.FileResponseHandler;
import com.piesat.outsideinvestigate.okhttp.promise.responsehandler.ResponseHandler;
import com.piesat.outsideinvestigate.okhttp.promise.responsehandler.StringResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Promise<T> {

    private Promise startPromise;
    private Call call;
    private ResponseHandler<T> responseHandler;
    private ResultHandler resultHandler;
    //    private final Lock lock = new ReentrantLock();
//    private final Condition condition = lock.newCondition();
    private Map<Promise, ResultHandler> resultHandlerMap;
    private List<ResultHandler> list;

    private Promise(List<ResultHandler> list) {
        this.list = list;
    }

    public Promise(Call call, ResponseHandler<T> responseHandler) {
        this.call = call;
        this.responseHandler = responseHandler;
//        resultHandlerMap = new LinkedHashMap<>();
        list = new ArrayList<>();
        startPromise = this;
    }

//    public <E> Promise<E> then(ResultHandler<T, E> resultHandler) {
//        try {
//            Runner.getInstance().run(this, new ResultCallback() {
//                @Override
//                public void onResult(Response response) {
//
//                }
//            });
////            Response response = execute();
//            nextPromise = resultHandler.resolve(responseHandler.handle(response));
//            System.out.println("okhttp" + Thread.currentThread().getName());
//            condition.signal();
//            System.out.println("okhttp请求到了结果");
//        } catch (Exception e) {
//            resultHandler.reject(e);
//        }
//        System.out.println(Thread.currentThread().getName());
//        return nextPromise;
//    }

    public <E> Promise<E> then(ResultHandler<T, E> resultHandler) {
//        resultHandlerMap.put(this, resultHandler);
        list.add(resultHandler);
        Promise<E> ePromise = new Promise<>(list);
        ePromise.startPromise = startPromise;
        return ePromise;
    }

//    public <E> Promise<E> withHandler(ResultHandler<T, E> resultHandler){
//        this.resultHandler = resultHandler;
//    }

    public void start() {
        startPromise.execute(0);
        if (startPromise.responseHandler instanceof FileResponseHandler) {
            FileResponseHandler fileResponseHandler = (FileResponseHandler) startPromise.responseHandler;
            fileResponseHandler.setFileResultHandler(list.get(0));
        }
    }

    private void execute(int index) {
        call.enqueue(new Callback() {
            private final ResultHandler resultHandler = list.get(index);

            @Override
            public void onFailure(Call call, IOException e) {
                resultHandler.reject(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                T result = null;
                try {
                    result = responseHandler.handle(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (resultHandler != null) {
                    Promise promise = resultHandler.resolve(result);
                    if (promise != null) {
                        if (promise.responseHandler instanceof FileResponseHandler) {
                            FileResponseHandler fileResponseHandler = (FileResponseHandler) promise.responseHandler;
                            fileResponseHandler.setFileResultHandler(resultHandler);
                        }
                        promise.list = list;
                        if (list.get(index + 1) != null) {
                            promise.execute(index + 1);
                        }
                    }
                }
            }
        });
    }


//    public <E> Promise<E> then(ResultHandler<T, E> resultHandler) {
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                resultHandler.reject(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                lock.lock();
//                try {
//                    Thread.sleep(1000);
//                    nextPromise = resultHandler.resolve(responseHandler.handle(response));
//                    System.out.println("okhttp:" + Thread.currentThread().getName());
//                    condition.signal();
//                    System.out.println("okhttp请求到了结果");
//                } catch (Exception e) {
//                    resultHandler.reject(e);
//                } finally {
//                    lock.unlock();
//                }
//            }
//        });
//        lock.lock();
//        try {
//            System.out.println("等待Okhttp返回结果");
//            condition.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }
//        System.out.println(Thread.currentThread().getName());
//        return nextPromise;
//    }

    public static void main(String[] args) {
//        test1();
        test2();
    }

    private static void test3() {
        OkhttpRequest.get("https://dldir1.qq.com/WechatWebDev/nightly/p-ae42ee2cde4d42ee80ac60b35f183a99/wechat_devtools_1.02.1911180_x64.exe")
                .async(new File("d:/"), new DownloadProgress() {
                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onSuccess(File result) {
                        OkhttpRequest.get("http://www.baidu.com")
                                .async(new HttpCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {

                                    }

                                    @Override
                                    public void onFailure(Exception e) {

                                    }
                                });
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

        try {
            String sync = OkhttpRequest.get("http://www.baidu.com").sync();
            String result = OkhttpRequest.get("http://www.163.com").sync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void test2() {
        OkhttpRequest.get("https://dldir1.qq.com/WechatWebDev/nightly/p-ae42ee2cde4d42ee80ac60b35f183a99/wechat_devtools_1.02.1911180_x64.exe")
                .toPromise(new FileResponseHandler(new File("d:/")))
                .then(new FileResultHandler<String>() {
                    @Override
                    public void onProgress(int progress) {
                        System.out.println(progress);
                    }

                    @Override
                    public Promise<String> resolve(File result) {
                        System.out.println(result.getAbsolutePath());
                        Promise<String> promise = OkhttpRequest.get("http://192.168.1.23:8016/monitor/windyInfo/getJsonById?id=947")
                                .toPromise(OkhttpRequest.STRING_RESPONSE_HANDLER);
                        return promise;
                    }

                    @Override
                    public void reject(Exception e) {

                    }
                })
                .then(new ResultHandler<String, String>() {
                    @Override
                    public Promise<String> resolve(String result) {
                        System.out.println(result);
                        return null;
                    }

                    @Override
                    public void reject(Exception e) {

                    }
                })
                .start();
    }

    private static void test1() {
        Promise<String> promise = new Promise<String>(
                OkhttpRequest.getCall("http://www.baidu.com"),
                new StringResponseHandler());
        promise
                .then(new ResultHandler<String, File>() {
                    @Override
                    public Promise<File> resolve(String result) {
//                        System.out.println(result);
                        System.out.println("baidu");
                        return new Promise<File>(OkhttpRequest.getCall("https://codeload.github.com/GaoGersy/wx-shulte/zip/master"),
                                new FileResponseHandler(new File("d:/")));
                    }

                    @Override
                    public void reject(Exception e) {

                    }
                })
                .then(new FileResultHandler<String>() {
                    @Override
                    public void onProgress(int progress) {
                        System.out.println(progress);
                    }

                    @Override
                    public Promise<String> resolve(File result) {
//                        System.out.println(result.getName());
                        System.out.println("163");
                        return new Promise<String>(OkhttpRequest.getCall("http://www.qq.com"),
                                new StringResponseHandler());
                    }

                    @Override
                    public void reject(Exception e) {

                    }
                })
                .then(new ResultHandler<String, String>() {
                    @Override
                    public Promise<String> resolve(String result) {
                        System.out.println("qq");
                        return new Promise<String>(OkhttpRequest.getCall("http://www.qq.com"),
                                new StringResponseHandler());
                    }

                    @Override
                    public void reject(Exception e) {

                    }
                })
                .then(new ResultHandler<String, String>() {
                    @Override
                    public Promise<String> resolve(String result) {
                        return null;
                    }

                    @Override
                    public void reject(Exception e) {

                    }
                })
                .start();
        System.out.println("请求完成！");
    }
}
