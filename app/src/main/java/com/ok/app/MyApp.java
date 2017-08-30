package com.ok.app;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by yzd on 2017/8/21 0021.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100000L, TimeUnit.MILLISECONDS)
                .readTimeout(100000L,TimeUnit.MILLISECONDS)
                .build();
        OkUtils.init(client);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
