package com.gersy.smartled.okhttp.promise.runthread;


import com.piesat.outsideinvestigate.okhttp.promise.Promise;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Runner {
    private static ThreadPoolExecutor THREAD_POOL_EXECUTOR;
    private AtomicInteger number = new AtomicInteger(0);
    private static Runner instance = null;

    private Runner() {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                1,
                3,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(10),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NotNull Runnable r) {
                        return new Thread(r, "Promise" + number.getAndIncrement());
                    }
                });
    }

    public static Runner getInstance() {
        if (instance==null){
            synchronized (Runner.class){
                if (instance==null){
                    instance=new Runner();
                }
            }
        }
        return instance;
    }

    public void run(Promise promise, ResultCallback resultCallback){
        PromiseRunnable promiseRunnable = new PromiseRunnable(promise, resultCallback);
//        PromiseCallable promiseCallable = new PromiseCallable(promise);
        THREAD_POOL_EXECUTOR.execute(promiseRunnable);
    }

//    public Response run(Promise promise){
//        PromiseRunnable promiseRunnable = new PromiseRunnable(promise, resultHandler);
////        PromiseCallable promiseCallable = new PromiseCallable(promise);
//        THREAD_POOL_EXECUTOR.execute(promiseRunnable);
//        THREAD_POOL_EXECUTOR.
//        try {
//            Response response = submit.get();
//            submit.
//            return response;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public class PromiseCallable implements Callable<Response>{
//        private Promise promise;
//
//        public PromiseCallable(Promise promise) {
//            this.promise = promise;
//        }
//        @Override
//        public Response call() throws Exception {
//            Thread.sleep(10000);
//            System.out.println("aaaaaaaa: "+Thread.currentThread().getName());
//            return promise.execute();
//        }
//    }

    public static class PromiseRunnable implements Runnable{
        private Promise promise;
        private ResultCallback resultCallback;

        public PromiseRunnable(Promise promise,ResultCallback resultCallback) {
            this.promise = promise;
            this.resultCallback = resultCallback;
        }

        @Override
        public void run() {
//            try {
////                Response response = promise.execute();
////                resultCallback.onResult(response);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}
