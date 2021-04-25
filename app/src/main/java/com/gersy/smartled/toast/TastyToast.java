package com.gersy.smartled.toast;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public enum TastyToast {
    SUCCESS(1),
    INFO(2),
    WARNING(3),
    ERROR(4),
    NORMAL(5);
    private int type;
//    private View view;
//    private Toast toast;


    TastyToast(int type) {
        this.type = type;
    }

    public Toast getLongToast(Context context, String msg) {
        return getToast(context, msg, Toast.LENGTH_LONG);
    }

    public Toast getShortToast(Context context, String msg) {
        return getToast(context, msg, Toast.LENGTH_SHORT);
    }

    public Toast getToast(Context context, String msg, int duration) {
        switch (type) {
            case 1:
                return Toasty.success(context, msg, duration);
            case 2:
                return Toasty.info(context, msg, duration);
            case 3:
                return Toasty.warning(context, msg, duration);
            case 4:
                return Toasty.error(context, msg, duration);
            case 5:
                return Toasty.normal(context, msg, duration);
        }
        return null;
    }

    public void showToast(final Context context, final String msg) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    getShortToast(context, msg).show();
                }
            });
        }else {
            getShortToast(context, msg).show();
        }
    }
}