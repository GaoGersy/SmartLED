package com.gersy.smartled.config;

import android.content.Context;
import android.os.Environment;


import com.gersy.smartled.app.SmartLEDApplication;
import com.gersy.smartled.utils.SpfUtils;

import java.io.File;

/**
 * Created by gersy on 2017/7/1.
 */

public class ConfigManager {
    public final Context mContext;
    private String RTK_ADDRESS = "rtk_address";
    private String RTK_ENABLE = "rtk_enable";
    private String RTK_TIME = "rtk_time";
    private String GPS_INTERVAL = "gps_interval";
    private String SCREEN_ROTATION = "screen_rotation";

    private ConfigManager() {
        mContext = SmartLEDApplication.getContext();
    }

    private static final ConfigManager singleInstance = new ConfigManager();

    public static ConfigManager getInstance() {
        return singleInstance;
    }

    public String getFileDirectory() {
        return Environment.getExternalStorageDirectory() + "/outside/";
    }


}
