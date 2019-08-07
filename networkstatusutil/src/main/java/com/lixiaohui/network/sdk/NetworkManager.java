package com.lixiaohui.network.sdk;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.lixiaohui.network.sdk.utils.Constants;

/**
 * @Author: Lee
 * @Date: 2019-08-07
 * @Desc:
 */
public class NetworkManager {
    private static volatile NetworkManager instance;
    private Application application;
    private NetworkStatusReceiver receiver;

    private NetworkManager() {
        receiver = new NetworkStatusReceiver();
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
        if (application == null) {
            throw new RuntimeException("NetworkStatusManager getApplication错误了");
        }
        return application;
    }

    @SuppressLint("MissingPermission")
    public void init(Application application) {
        this.application = application;
        //广播注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback callback = new NetworkStatusCallBack();
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cmgr = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cmgr != null) {
                cmgr.registerNetworkCallback(request, callback);
            }
        } else {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
            application.registerReceiver(receiver, filter);
        }

    }

    public void registerObserver(Object register) {
        receiver.registerObserver(register);
    }

    public void unregisterObserver(Object register) {
        receiver.unregisterObserver(register);
    }

    public void unregisterAllObserver() {
        receiver.unregisterAllObserver();
    }

}
