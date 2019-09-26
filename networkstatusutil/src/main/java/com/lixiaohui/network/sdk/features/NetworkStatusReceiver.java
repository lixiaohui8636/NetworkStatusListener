package com.lixiaohui.network.sdk.features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lixiaohui.network.sdk.utils.Constants;
import com.lixiaohui.network.sdk.utils.NetworkUtils;

/**
 * @Author: Lee
 * @Date: 2019-08-07
 * @Desc:
 */
public class NetworkStatusReceiver extends BroadcastReceiver {
    private NetType netType;

    public NetworkStatusReceiver() {
        this.netType =NetType.DISABLE;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getAction() == null){
            return;
        }
        //处理广播事件
        if(Constants.ANDROID_NET_CHANGE_ACTION.equalsIgnoreCase(intent.getAction())){
            netType = null;
            if(NetworkUtils.isNetworkAvailable(context)){
                netType = NetworkUtils.getNetType(context);
            }else{
                netType = NetType.DISABLE;
            }
        }
        //通知所有注册的方法，网络发生变化
        ObserverManager.getInstance().post(netType);
    }
}
