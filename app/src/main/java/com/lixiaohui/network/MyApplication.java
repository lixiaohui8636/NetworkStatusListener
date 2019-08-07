package com.lixiaohui.network;

import android.app.Application;

import com.lixiaohui.network.sdk.NetworkManager;

/**
 * @Author: Lee
 * @Date: 2019-08-07
 * @Desc:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getInstance().init(this);
    }
}
