package com.lyhome.ETWeather.utility;

import android.app.Application;
import android.content.Context;

/**
 * Created by lyhomeLY on 1/15/16.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
