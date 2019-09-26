package com.lixiaohui.network.sdk;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.lixiaohui.network.sdk.features.NetworkStatusCallback;
import com.lixiaohui.network.sdk.features.NetworkStatusReceiver;
import com.lixiaohui.network.sdk.features.ObserverManager;
import com.lixiaohui.network.sdk.utils.Constants;

/**
 * @Author: Lee
 * @Date: 2019-08-07
 * @Desc:
 */
public class NetworkManager {
    private static volatile NetworkManager instance;
    private Application application;

    //5.0以上用这个
    private NetworkStatusCallback networkStatusCallback;
    //5，0一下用receiver
    private NetworkStatusReceiver networkStatusReceiver;

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    public Application getApplication() {
        return application;
    }

    @SuppressLint("MissingPermission")
    public void init(Application application) {
        this.application = application;
        //广播注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkStatusCallback = new NetworkStatusCallback();
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cmgr = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cmgr != null) {
                cmgr.registerNetworkCallback(request, networkStatusCallback);
            }
        } else {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
            networkStatusReceiver = new NetworkStatusReceiver();
            application.registerReceiver(networkStatusReceiver, filter);
        }

    }

    public void registerObserver(Object register) {
        ObserverManager.getInstance().registerObserver(register);
    }

    public void unregisterObserver(Object register) {
        ObserverManager.getInstance().unregisterObserver(register);
    }

    public void unregisterAllObserver() {
        ObserverManager.getInstance().unregisterAllObserver();
        if (networkStatusReceiver != null) {
            application.unregisterReceiver(networkStatusReceiver);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && networkStatusCallback != null) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cmgr = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cmgr != null) {
                cmgr.registerNetworkCallback(request, networkStatusCallback);
            }
        }
    }

}
