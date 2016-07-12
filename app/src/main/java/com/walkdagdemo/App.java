package com.walkdagdemo;

import android.app.Application;

import com.wilddog.client.Wilddog;

/**
 * Created by 野狗相关 on 2016/7/7.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Wilddog.setAndroidContext(this);
    }
}
