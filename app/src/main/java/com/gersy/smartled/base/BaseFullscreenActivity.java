package com.gersy.smartled.base;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by a3266 on 2017/6/11.
 */

public abstract class BaseFullscreenActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

}
