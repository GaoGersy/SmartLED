package com.gersy.smartled.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogStrategy;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import es.dmoral.toasty.Toasty;

public class SmartLEDApplication extends Application {
    //上下文
    public static Context mContext;

    //主线程的handler
    public static Handler mHandler;

    /**
     * 得到上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 得到主线程handler对象
     *
     * @return
     */
    public static Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onCreate() {
        //初始化上下文
        mContext = getApplicationContext();
//        CrashReport.initCrashReport(getApplicationContext(), "83a16f6a72", false);
        //初始化主线程的一个handler
        mHandler = new Handler();
        initLogger();
        initToast();
        super.onCreate();

    }

    private void initLogger() {
        DiskLogStrategy strategy = new DiskLogStrategy(mHandler);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(new LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("outside")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    private void initToast() {
        Toasty.Config.getInstance()
                .tintIcon(true) // optional (apply textColor also to the icon)
                .setTextSize(15) // optional
                .allowQueue(true) // optional (prevents several Toastys from queuing)
                .apply(); // required
    }
}
